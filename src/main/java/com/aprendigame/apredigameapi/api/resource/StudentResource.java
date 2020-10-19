package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.StudentDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentResource {
	
	private StudentService service;
	
	public StudentResource(StudentService service) {
		this.service = service;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Serializable> authenticate( @RequestBody StudentDTO dto) {
		try {
			Student authenticatedStudent = service.authenticate(dto.getRegistration(), dto.getPassword());
			return ResponseEntity.ok(authenticatedStudent);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<Serializable> save( @RequestBody StudentDTO dto) {
		 
		Student student = new Student();
		student.setName(dto.getName());
		student.setRegistration(dto.getRegistration());
		student.setPhoto(dto.getPhoto());
		student.setSchoolName(dto.getSchoolName());
		student.setCourseUnit(dto.getCourseUnit());
		student.setBirthday(dto.getBirthday());
		student.setPassword(dto.getPassword());
		student.setActualLevel(1);
		student.setNextLevel(2);
		student.setPoints(0);
		student.setRequiredPoints(student.getActualLevel()*25);
		
		try {
			Student savedStudent = service.saveStudent(student);
			
			return new ResponseEntity<Serializable>(savedStudent, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
