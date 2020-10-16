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
import com.aprendigame.apredigameapi.service.PresencService;

@RestController
@RequestMapping("/api/presenc")
public class PresencResource {

	private PresencService service;
	
	public PresencResource(PresencService service) {
		this.service = service;
	}
	
	@PostMapping("/find")
	public ResponseEntity<Serializable> authenticate(@RequestBody PresencDTO dto){
		try {
			Presenc authenticatedPresenc = service.authenticate(dto.getCode(), dto.getStudent());
			return ResponseEntity.ok(authenticatedPresenc);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody PresencDTO dto){
		Presenc presenc = new Presenc();
		presenc.setCode(dto.getCode());
		presenc.setCourseClassCode(dto.getCourseClassCode());
		presenc.setDate(dto.getDate());
		presenc.setHour(dto.getHour());
		presenc.setStudent(dto.getStudent());
		
		try {
			Presenc savedPresenc = service.savePresenc(presenc);
			
			return new ResponseEntity<Serializable>(savedPresenc, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
