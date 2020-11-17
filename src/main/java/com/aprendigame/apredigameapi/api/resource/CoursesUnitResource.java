package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CoursesUnitDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.StudentService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/courseUnit")
public class CoursesUnitResource {

	private CoursesUnitService service;
	private TeacherService serviceTeacher;
	private TeacherResource teacherResource;
	private StudentService studentService;
	private CourseClassResource courseClassResource;
	
	public CoursesUnitResource(CoursesUnitService service, TeacherService serviceTeacher, TeacherResource teacherResource, StudentService studentService, CourseClassResource courseClassResource) {
		this.service = service;
		this.serviceTeacher = serviceTeacher;
		this.teacherResource = teacherResource;
		this.studentService = studentService;
		this.courseClassResource = courseClassResource;
	}
	

	@GetMapping
	public ResponseEntity<Object> findCourseUnit(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "teacherId", required = false) Long teacherId,
			@RequestParam(value = "code", required = false) String code) {
		
		CoursesUnit courseUnitFilter = new CoursesUnit();
		
		courseUnitFilter.setName(name);
		courseUnitFilter.setCode(code);
		
		
		List<CoursesUnit> coursesUnits = service.search(courseUnitFilter);
		List<CoursesUnit> coursesUnitsFiltred = new ArrayList<CoursesUnit>();
		
		if(teacherId != null) {
			Teacher teacher = serviceTeacher.findById(teacherId)
					.orElseThrow(() -> new BusinessRuleException("Não foi econtrado o professor no nosso banco de dados"));
			for (CoursesUnit courseUnit : coursesUnits) {
				for(Teacher teacher1 : courseUnit.getTeachers()) {
					if(teacher1.getId().equals(teacher.getId())) {
						coursesUnitsFiltred.add(courseUnit);
					}
				}
			}
			return ResponseEntity.ok(coursesUnitsFiltred);
		}
		
		
		return ResponseEntity.ok(coursesUnits);	
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id")Long id) {
		return service.findById(id).map(entity -> {
			for (Teacher teacher : entity.getTeachers()) {
				teacherResource.removeTeacherFromCourseUnit(teacher.getId(), entity);
			}
			
			for (Student student : entity.getStudents()) {
				student.setCourseUnit(null);
				studentService.updateStudent(student);
			}
			
			for (CourseClass courseClass : entity.getCourseClasses()) {
				courseClassResource.delete(courseClass.getId());	
			}
			
			service.deleteCourseUnit(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity("Curso não encontrado", HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<Optional<CoursesUnit>> getCourseUnit(@PathVariable("id") Long id) {
		try {
			Optional<CoursesUnit> courseUnit = service.findById(id);
			return new ResponseEntity<Optional<CoursesUnit>>(courseUnit, HttpStatus.OK);
		} catch (BusinessRuleException e) {
			return new ResponseEntity<Optional<CoursesUnit>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id") Long id, @RequestBody CoursesUnitDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit courseUnit = convert(dto);
				courseUnit.setId(entity.getId());
				
				if (entity.getTeachers() != null || !entity.getTeachers().isEmpty()) {
					courseUnit.setTeachers(entity.getTeachers());
				}
				
				if (entity.getStudents() != null || !entity.getStudents().isEmpty()) {
					courseUnit.setStudents(entity.getStudents());
				}
				courseUnit.setCourseClasses(entity.getCourseClasses());
				
				service.updateCourseUnit(courseUnit);
				return ResponseEntity.ok(courseUnit);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Não foi possivel encontra o curso", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@PutMapping("/{id}/includeteacher")
	public ResponseEntity includeTeacher(@PathVariable("id") Long id, @RequestBody String teacherRegistration) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit coursesUnit = entity;
				
				Optional<Teacher> findTeacher = serviceTeacher.findByRegistration(teacherRegistration);
				if (!findTeacher.isPresent()) {
					return ResponseEntity.badRequest().body("Professor não encontrado para a matricula informada");
				} 
				
				List<Teacher> teachers = coursesUnit.getTeachers();
				
				if (teachers.contains(findTeacher.get())) {
					return ResponseEntity.badRequest().body("Professor já está no Curso");
				}
				
				teachers.add(findTeacher.get());
				coursesUnit.setTeachers(teachers);
				
				ResponseEntity response = teacherResource.includeTeacherInCourseUnit(findTeacher.get().getId(), coursesUnit);
				
				if (response.equals(ResponseEntity.ok(findTeacher.get()))) {
					service.updateCourseUnit(coursesUnit);
					return ResponseEntity.ok(findTeacher);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@PutMapping("/{id}/removeteacher/{teacherId}")
	public ResponseEntity removeTeacher(@PathVariable("id") Long id, @PathVariable("teacherId") Long teacherId) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit coursesUnit = entity;
				
				Optional<Teacher> findTeacher = serviceTeacher.findById(teacherId);
				
				if (!findTeacher.isPresent()) {
					return ResponseEntity.badRequest().body("Professor não encontrado para a matricula informada");
				} 
				
				List<Teacher> teachers = coursesUnit.getTeachers();
				
				if (!teachers.contains(findTeacher.get())) {
					return ResponseEntity.badRequest().body("Professor já está no Curso");
				}
				
				teachers.remove(findTeacher.get());
				coursesUnit.setTeachers(teachers);
				
				ResponseEntity response = teacherResource.removeTeacherFromCourseUnit(findTeacher.get().getId(), coursesUnit);
				
				if (response.equals(ResponseEntity.ok(findTeacher.get()))) {
					service.updateCourseUnit(coursesUnit);
					return ResponseEntity.ok(coursesUnit);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/includestudent")
	public ResponseEntity includeStudent(@PathVariable("id") Long id, @RequestBody String studentRegistration) {
		return service.findById(id).map(entity -> {
			CoursesUnit coursesUnit = entity;
			
			Optional<Student> student = studentService.findByRegistration(studentRegistration);
			
			if (!student.isPresent()) {
				return ResponseEntity.badRequest().body("Estudando não encontrada para matricula informada");
			}
			
			if (student.get().getCourseUnit() != null) {
				return ResponseEntity.badRequest().body("Estudando já pertence a um Curso");
			}
			
			List<Student> students = coursesUnit.getStudents();
			
			if (students.contains(student.get())) {
				return ResponseEntity.badRequest().body("Estudando já pertence a esse Curso");
			}
			
			students.add(student.get());
			student.get().setCourseUnit(coursesUnit);
			studentService.updateStudent(student.get());
			
			service.updateCourseUnit(coursesUnit);
			return ResponseEntity.ok(student);
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/removestudent/{studentId}")
	public ResponseEntity removeStudent(@PathVariable("id") Long id, @PathVariable("studentId") Long studentId) {
		return service.findById(id).map(entity -> {
			CoursesUnit coursesUnit = entity;
			
			Student student = studentService.findById(studentId).orElseThrow(() -> new BusinessRuleException("Estudante não encontrado!"));
			
			
			List<Student> studentList = coursesUnit.getStudents();
			
			if (!studentList.contains(student)) {
				return ResponseEntity.badRequest().body("Esse estudante não pertence ao curso");
			}
			
			if (!student.getCourseUnit().getId().equals(coursesUnit.getId())) {
				return ResponseEntity.badRequest().body("Esse estudante não pertence ao curso");
			}
			
			studentList.remove(student);
			student.setCourseUnit(null);
			
			studentService.updateStudent(student);
			
			coursesUnit.setStudents(studentList);
			service.updateCourseUnit(coursesUnit);
			return ResponseEntity.ok(student);
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> saveCourseClass(@RequestBody CoursesUnitDTO dto) {		
		try {
			CoursesUnit savedCourseUnit = convert(dto);

			Optional<Teacher> teacher = serviceTeacher.findById(dto.getTeacherId());
			if(teacher.isPresent()) {
				List<Teacher> teachers = new ArrayList<>();
				teachers.add(teacher.get());
				savedCourseUnit.setTeachers(teachers);
			}
			
			savedCourseUnit = service.saveCoursesUnit(savedCourseUnit);
			teacherResource.includeTeacherInCourseUnit(teacher.get().getId(), savedCourseUnit);
			return new ResponseEntity<Serializable>(savedCourseUnit, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	private CoursesUnit convert(CoursesUnitDTO dto) {
		CoursesUnit courseUnit = new CoursesUnit();
		courseUnit.setCode(dto.getCode());
		courseUnit.setName(dto.getName());
		
		
		return courseUnit;
	}
}
