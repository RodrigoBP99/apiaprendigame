package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@GetMapping("/{code}")
	public ResponseEntity<Object> findCourseClass(@PathVariable("code") String code) {
		try {
			Optional<CourseClass> courseClass = service.findByCode(code);

			return new ResponseEntity<Object>(courseClass, HttpStatus.FOUND);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{code}")
	public ResponseEntity update(@PathVariable("code")String code, @RequestBody CourseClassDTO dto ) {
		return service.findByCode(code).map(entity -> {
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
	@PutMapping("/{code}/include_student")
	public ResponseEntity includeStudentOnCourseClass(@PathVariable("code") String code, @RequestBody CourseClassDTO dto) {
		return service.findByCode(code).map(entity -> {
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
