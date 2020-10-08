package com.aprendigame.apredigameapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.StudentDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentResource {
	
	private StudentService service;
	
	public StudentResource(StudentService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity save( @RequestBody StudentDTO dto) {
		 
		Student student = new Student();
		student.setName(dto.getName());
		student.setRegistration(dto.getRegistration());
		student.setSchoolName(dto.getSchoolName());
		student.setCourseName(dto.getSchoolName());
		student.setBirthday(dto.getBirthday());
		student.setPassword(dto.getPassword());
		
		try {
			Student savedStudent = service.saveStudent(student);
			
			return new ResponseEntity(savedStudent, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
