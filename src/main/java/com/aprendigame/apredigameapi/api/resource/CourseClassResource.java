package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CourseClassDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CourseClassService;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/courseClass")
public class CourseClassResource {

	private CourseClassService service;
	
	private CoursesUnitService courseUnitService;
	
	private TeacherService teacherService;
	
	public CourseClassResource(CourseClassService service, CoursesUnitService courseUnitService, TeacherService teacherService) {
		this.service = service;
		this.courseUnitService = courseUnitService;
		this.teacherService = teacherService;
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
	
	private CourseClass convert(CourseClassDTO dto) {
		CourseClass courseClass = new CourseClass();
		courseClass.setName(dto.getName());
		courseClass.setCode(dto.getCode());
		courseClass.setStudents(dto.getStudents());
		courseClass.setQuizzes(dto.getQuizzes());
		
		
		CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
				.orElseThrow(() -> new BusinessRuleException("Não foi encontrado nenhum curso com esse código"));
		
		courseClass.setCourseUnit(courseUnit);
		
		Optional<Teacher> teacher = teacherService.findByRegistration(dto.getTeacherRegistration());
		
		courseClass.setTeacher(teacher.get());
		
		return courseClass;
	}
}
