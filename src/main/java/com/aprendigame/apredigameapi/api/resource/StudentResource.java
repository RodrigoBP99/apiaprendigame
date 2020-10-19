package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.StudentDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentResource {
	
	private StudentService service;
	private CoursesUnitService courseUnitService;
	
	public StudentResource(StudentService service, CoursesUnitService courseUnitService) {
		this.service = service;
		this.courseUnitService = courseUnitService;
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
		try {
			Student savedStudent = convert(dto);
			savedStudent = service.saveStudent(savedStudent);
			
			return new ResponseEntity<Serializable>(savedStudent, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/update/{registration}")
	public ResponseEntity updateStudent(@PathVariable("registration") String registration, @RequestBody StudentDTO dto) {
		return service.findByRegistration(registration).map(entity -> {
			try {
				Student student = convert(dto);
				student.setId(entity.getId());
				student.setRegistration(entity.getRegistration());
				service.updateStudent(student);
				return ResponseEntity.ok(student);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
				new ResponseEntity("Estudante não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	private Student convert(StudentDTO dto) {
		Student student = new Student();
		student.setName(dto.getName());
		student.setRegistration(dto.getRegistration());
		student.setPhoto(dto.getPhoto());
		student.setSchoolName(dto.getSchoolName());
		student.setBirthday(dto.getBirthday());
		student.setPassword(dto.getPassword());
		student.setActualLevel(1);
		student.setNextLevel(2);
		student.setPoints(0);
		student.setRequiredPoints(student.getActualLevel()*25);
		
		CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
				.orElseThrow(() -> new BusinessRuleException("Curso não encontrado com esse Código"));
	
		student.setCourseUnit(courseUnit);
		
		return student;
	}
}
