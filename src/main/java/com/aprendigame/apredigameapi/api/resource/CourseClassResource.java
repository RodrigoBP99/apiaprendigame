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
			@RequestParam("teacher")String teacher) {
		
		CourseClass courseClassFilter = new CourseClass();
		courseClassFilter.setName(name);
		
		Optional<Teacher> teacherCourseClass = teacherService.findByRegistration(teacher);
		if(!teacherCourseClass.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel encontra nenhum professor com esse codigo");
		} else {
			courseClassFilter.setTeacher(teacherCourseClass.get());
		}
		
		Optional<CoursesUnit> courseUnitCourseClass = courseUnitService.findByCode(courseUnit);
		if(courseUnitCourseClass.isPresent()) {
			courseClassFilter.setCourseUnit(courseUnitCourseClass.get());
		}
		
		List<CourseClass> courseClasses = service.search(courseClassFilter);
		return ResponseEntity.ok(courseClasses);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("{id}")
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

	@GetMapping("/{id}")
	public ResponseEntity<Object> findCourseClass(@PathVariable("id") Long id) {
		try {
			Optional<CourseClass> courseClass = service.findById(id);

			return new ResponseEntity<Object>(courseClass, HttpStatus.FOUND);
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
				courseClass.setCode(entity.getCode());
				courseClass.setStudents(entity.getStudents());
				courseClass.setQuizzes(entity.getQuizzes());
				
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/include_teacher")
	public ResponseEntity includeTeacherOnCourseClass(@PathVariable("id") Long id, @RequestBody CourseClassDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Teacher teacher = teacherService.findByRegistration(dto.getTeacherRegistration())
						.orElseThrow(() -> new BusinessRuleException("Professor não encontrado para essa matricula"));

				courseClass.setTeacher(teacher);

				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/remove_teacher")
	public ResponseEntity removeTeacherOnCourseClass(@PathVariable("id") Long id, @RequestBody CourseClassDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Teacher teacher = teacherService.findByRegistration(dto.getTeacherRegistration())
						.orElseThrow(() -> new BusinessRuleException("Professor não encontrado para essa matricula"));

				if (courseClass.getTeacher().equals(teacher)) {
					courseClass.setTeacher(null);
					service.updateCourseClass(courseClass);
					return ResponseEntity.ok(courseClass);
				}else {
					throw new BusinessRuleException("Esse não é o professor dessa matéria");
				}
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

		Optional<Teacher> teacher = teacherService.findByRegistration(dto.getTeacherRegistration());
		if (teacher.isPresent()) {
			courseClass.setTeacher(teacher.get());
		}
		return courseClass;
	}
}
