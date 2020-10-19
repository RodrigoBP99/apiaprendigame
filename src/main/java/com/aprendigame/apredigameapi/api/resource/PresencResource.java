package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.PresencDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.PresencService;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/presenc")
public class PresencResource {

	private PresencService service;
	private StudentService studentService;
	
	public PresencResource(PresencService service, StudentService studentService) {
		this.service = service;
		this.studentService = studentService;
	}
	
	@PostMapping("/find")
	public ResponseEntity<Serializable> authenticate(@RequestBody PresencDTO dto){
		try {
			
			Presenc authenticatedPresenc = convert(dto);
			authenticatedPresenc = service.authenticate(authenticatedPresenc.getCode(), authenticatedPresenc.getStudent());
			return ResponseEntity.ok(authenticatedPresenc);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody PresencDTO dto){		
		try {
			Presenc savedPresenc = convert(dto);
			savedPresenc = service.savePresenc(savedPresenc);
			
			return new ResponseEntity<Serializable>(savedPresenc, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	private Presenc convert(PresencDTO dto) {
		Presenc presenc = new Presenc();
		presenc.setCode(dto.getCode());
		presenc.setCourseClassCode(dto.getCourseClassCode());
		presenc.setDate(dto.getDate());
		presenc.setHour(dto.getHour());
		
		Student student = studentService.findByRegistration(dto.getStudent())
				.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para a matricula informada"));
		
		presenc.setStudent(student);
		
		return presenc;
	}
}
