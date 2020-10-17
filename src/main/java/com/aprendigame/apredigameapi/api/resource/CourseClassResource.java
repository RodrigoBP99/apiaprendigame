package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CourseClassDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.service.CourseClassService;

@RestController
@RequestMapping("api/courseClass")
public class CourseClassResource {

	private CourseClassService service;
	
	public CourseClassResource(CourseClassService service) {
		this.service = service;
	}
	
	@PostMapping("/find")
	public ResponseEntity<Serializable> findCourseClass(@RequestBody CourseClassDTO dto) {
		try {
			CourseClass authenticatedCourseClass = service.findCourseClass(dto.getCode(), dto.getCourseUnit());
			return ResponseEntity.ok(authenticatedCourseClass);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody CourseClassDTO dto) {
		CourseClass courseClass = new CourseClass();
		courseClass.setName(dto.getName());
		courseClass.setCode(dto.getCode());
		courseClass.setStudents(dto.getStudents());
		courseClass.setCourseUnit(dto.getCourseUnit());
		courseClass.setTeacher(dto.getTeacher());
		
		
		try {
			CourseClass savedCourseClass = service.saveCourseClass(courseClass);
			
			return new ResponseEntity<Serializable>(savedCourseClass, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
