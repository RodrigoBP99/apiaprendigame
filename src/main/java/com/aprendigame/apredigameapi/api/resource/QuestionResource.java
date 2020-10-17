package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.QuestionDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Question;
import com.aprendigame.apredigameapi.service.QuestionService;

@RestController
@RequestMapping("/api/question")
public class QuestionResource {

	private QuestionService service;
	
	public QuestionResource(QuestionService service) {
		this.service = service;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody QuestionDTO dto) {
		Question question = new Question();
		question.setQuestionTittle(dto.getQuestionTittle());
		question.setAnswers(dto.getAnswers());
		question.setQuizz(dto.getQuizz());
		
		try {
			Question savedQuestion = service.saveQuestion(question);
			
			return new ResponseEntity<Serializable>(savedQuestion, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
