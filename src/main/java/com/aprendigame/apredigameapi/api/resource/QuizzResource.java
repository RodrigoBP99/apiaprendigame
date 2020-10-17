package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.QuizzDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.service.QuizzService;

@RestController
@RequestMapping("/api/quizz")
public class QuizzResource {

	private QuizzService service;
	
	public QuizzResource(QuizzService service) {
		this.service = service;
	}
	
	@PostMapping("/find")
	public ResponseEntity<Serializable> find(@RequestBody QuizzDTO dto){
		try {
			Quizz authenticatedQuizz = service.findQuizz(dto.getCode(), dto.getCourseClass());
			
			return ResponseEntity.ok(authenticatedQuizz);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody QuizzDTO dto){
		Quizz quizz = new Quizz();
		quizz.setCode(dto.getCode());
		quizz.setTitle(dto.getTitle());
		quizz.setCourseClass(dto.getCourseClass());
		
		try {
			Quizz savedQuizz = service.saveQuizz(quizz);
			
			return new ResponseEntity<Serializable>(savedQuizz, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
