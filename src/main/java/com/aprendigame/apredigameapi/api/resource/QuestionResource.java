package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.QuestionDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Question;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.service.QuestionService;
import com.aprendigame.apredigameapi.service.QuizzService;

@RestController
@RequestMapping("/api/question")
public class QuestionResource {

	private QuestionService service;
	
	private QuizzService serviceQuizz;
	
	public QuestionResource(QuestionService service, QuizzService serviceQuizz) {
		this.service = service;
		this.serviceQuizz = serviceQuizz;
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Question> getQuestion(@PathVariable("id") Long id) {
		try {
			Question question = service.findById(id)
					.orElseThrow(() -> new BusinessRuleException("Questão não encontrada"));
			return new ResponseEntity<Question>(question, HttpStatus.OK);
		} catch (BusinessRuleException e) {
			return new ResponseEntity<Question>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody QuestionDTO dto) {
		Question question = new Question();
		question.setQuestionTittle(dto.getQuestionTittle());
		question.setAnswers(dto.getAnswers());
		
		Quizz quizz = serviceQuizz.findById(dto.getQuizzId())
				.orElseThrow(() -> new BusinessRuleException("Não foi possivel cadastrar a pergunta, o Quizz informado não existe"));
		
		question.setQuizz(quizz);
		
		if(!quizz.getQuestions().isEmpty()) {
			String amountOfQuestions = String.valueOf(quizz.getQuestions().size() + 1);
			quizz.setAmountOfQuestions(amountOfQuestions);
		}
		
		
		try {
			Question savedQuestion = service.saveQuestion(question);
			serviceQuizz.updateQuizz(quizz);
			
			return new ResponseEntity<Serializable>(savedQuestion, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
