package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
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

import com.aprendigame.apredigameapi.api.dto.CourseClassDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CourseClassService;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.StudentService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/courseClass")
public class CourseClassResource {

	private CourseClassService service;

	private CoursesUnitService courseUnitService;

	private TeacherService teacherService;

	private StudentService studentService;

	public CourseClassResource(CourseClassService service, CoursesUnitService courseUnitService,
			TeacherService teacherService, StudentService studentService) {
		this.service = service;
		this.courseUnitService = courseUnitService;
		this.teacherService = teacherService;
		this.studentService = studentService;
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity findCourseClass(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "courseUnit", required = false)String courseUnit,
			@RequestParam(value = "teacher", required = false)Long teacher) {
		
		CourseClass courseClassFilter = new CourseClass();
		courseClassFilter.setName(name);
		
		if(courseUnit != null) {
			Optional<CoursesUnit> courseUnitCourseClass = courseUnitService.findByCode(courseUnit);
			if(courseUnitCourseClass.isPresent()) {
				courseClassFilter.setCourseUnit(courseUnitCourseClass.get());
			}
		}
		
		if(teacher != null) {
			Optional<Teacher> teacherCourseClass = teacherService.findById(teacher);
			if(teacherCourseClass.isPresent()) {
				courseClassFilter.setTeacher(teacherCourseClass.get());
			}
		}
		
		List<CourseClass> courseClasses = service.search(courseClassFilter);
		
		return ResponseEntity.ok(courseClasses);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/getCourseClass/{id}")
	public ResponseEntity getCourseClass(@PathVariable("id") Long id) {
		try {
			Optional<CourseClass> courseClass = service.findById(id);
			return new ResponseEntity(courseClass.get(), HttpStatus.OK);	
		} catch (BusinessRuleException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/find/{id}")
	public ResponseEntity findToUpdate(@PathVariable("id") Long id) {
		try {
			Optional<CourseClass> courseClass = service.findById(id);
			CourseClassDTO courseClassDTO = convertDTO(courseClass.get());
			return new ResponseEntity(courseClassDTO, HttpStatus.OK);	
		} catch (BusinessRuleException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	private CourseClassDTO convertDTO(CourseClass courseClass) {
		CourseClassDTO dto = new CourseClassDTO();
		dto.setId(courseClass.getId());
		dto.setCode(courseClass.getCode());
		dto.setName(courseClass.getName());
		dto.setQuizzes(courseClass.getQuizzes());
		dto.setStudents(courseClass.getStudents());
		
		if(courseClass.getCourseUnit() != null) {
			dto.setCourseUnitCode(courseClass.getCourseUnit().getCode());
		}
		
		if(courseClass.getTeacher() != null) {
			dto.setTeacherId(courseClass.getTeacher().getId());
		}
		
		return dto;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.findById(id).map(entity -> {
			service.deleteCourseClass(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
			new ResponseEntity("Classe não encontrada no banco de dados", HttpStatus.BAD_REQUEST));
	}

	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody CourseClassDTO dto) {
		try {
			CourseClass savedCourseClass = convert(dto);
			savedCourseClass = service.saveCourseClass(savedCourseClass);
			return new ResponseEntity<Serializable>(savedCourseClass, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id")Long id, @RequestBody CourseClassDTO dto ) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = convert(dto);
				courseClass.setId(entity.getId());
				courseClass.setStudents(entity.getStudents());
				courseClass.setQuizzes(entity.getQuizzes());
				courseClass.setTeacher(entity.getTeacher());
				
				if(!dto.getCourseUnitCode().equals(entity.getCourseUnit().getCode())) {
					CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
							.orElseThrow(() -> new BusinessRuleException("Não foi encontrado nenhum curso com esse código"));
					service.validateCodeAndCourseUnit(entity.getCode(), courseUnit);
					
					if(!dto.getCode().equals(entity.getCode())) {
						service.validateCodeAndCourseUnit(dto.getCode(), courseUnit);
					} else {
						courseClass.setCode(entity.getCode());
					}
				} else {
					courseClass.setCourseUnit(entity.getCourseUnit());
				}
				
				if(!dto.getCode().equals(entity.getCode())) {
						service.validateCodeAndCourseUnit(dto.getCode(), entity.getCourseUnit());
				}
				
				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/include_student")
	public ResponseEntity includeStudentOnCourseClass(@PathVariable("id") Long id, @RequestBody CourseClassDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Student student = studentService.findByRegistration(dto.getStudentRegistration())
						.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para essa matricula"));

				List<Student> students = courseClass.getStudents();
				students.add(student);

				courseClass.setStudents(students);

				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/remove_student")
	public ResponseEntity removeStudentOnCourseClass(@PathVariable("id") Long id, @RequestBody CourseClassDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Student student = studentService.findByRegistration(dto.getStudentRegistration())
						.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para essa matricula"));

				List<Student> students = courseClass.getStudents();
				students.remove(student);

				courseClass.setStudents(students);

				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	

	private CourseClass convert(CourseClassDTO dto) {
		CourseClass courseClass = new CourseClass();
		courseClass.setName(dto.getName());
		courseClass.setCode(dto.getCode());
		courseClass.setQuizzes(dto.getQuizzes());

		CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
				.orElseThrow(() -> new BusinessRuleException("Não foi encontrado nenhum curso com esse código"));

		courseClass.setCourseUnit(courseUnit);

		Optional<Teacher> teacher = teacherService.findById(dto.getTeacherId());
		if (teacher.isPresent()) {
			courseClass.setTeacher(teacher.get());
		}
		return courseClass;
	}
}
