package com.aprendigame.apredigameapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.AnswerDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Answer;
import com.aprendigame.apredigameapi.model.entity.Question;
import com.aprendigame.apredigameapi.service.AnswerService;
import com.aprendigame.apredigameapi.service.QuestionService;

@RestController
@RequestMapping("/api/answer")
public class AnswerResource {

	private QuestionService serviceQuestion;
	private AnswerService service;
	
	public AnswerResource(AnswerService service, QuestionService serviceQuestion) {
		this.service = service;
		this.serviceQuestion = serviceQuestion;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Object> save(@RequestBody AnswerDTO dto) {
		Answer answer = new Answer();
		answer.setText(dto.getText());
		answer.setAnswerType(dto.getAnswerType());

	
		Question question = serviceQuestion.findById(dto.getQuestionId())
				.orElseThrow(() -> new BusinessRuleException("Não foi possivel encontrar a questão para adicionar essa resposta"));
		
		answer.setQuestion(question);
		
		try {
			Answer savedAnswer = service.saveAnswer(answer);
			
			return new ResponseEntity<Object>(savedAnswer, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
