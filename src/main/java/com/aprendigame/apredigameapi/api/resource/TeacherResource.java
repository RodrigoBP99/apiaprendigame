package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.TeacherDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherResource {

	private TeacherService service;
	
	public TeacherResource(TeacherService service) {
		this.service = service;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Serializable> authenticate(@RequestBody TeacherDTO dto){
		try {
			Teacher authenticatedTeacher = service.authenticate(dto.getRegistration(), dto.getPassword());
			return ResponseEntity.ok(authenticatedTeacher);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<Serializable> save(@RequestBody TeacherDTO dto){
		Teacher teacher = new Teacher();
		teacher.setName(dto.getName());
		teacher.setRegistration(dto.getRegistration());
		teacher.setPassword(dto.getPassword());
		
		try {
			Teacher savedTeacher = service.saveTeacher(teacher);
			
			return new ResponseEntity<Serializable>(savedTeacher, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
