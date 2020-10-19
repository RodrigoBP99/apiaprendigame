package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.QuizzDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.service.CourseClassService;
import com.aprendigame.apredigameapi.service.QuizzService;

@RestController
@RequestMapping("/api/quizz")
public class QuizzResource {

	private QuizzService service;
	
	private CourseClassService courseClassService;
	
	public QuizzResource(QuizzService service, CourseClassService courseClassService) {
		this.service = service;
		this.courseClassService = courseClassService;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody QuizzDTO dto){
		Quizz quizz = new Quizz();
		quizz.setCode(dto.getCode());
		quizz.setTitle(dto.getTitle());
		
		CourseClass courseClass = courseClassService.findByCode(dto.getCourseClassCode())
				.orElseThrow(() -> new BusinessRuleException("Não foi possivel concluir a ação, matéria não encontrada no sistema"));
		
		quizz.setCourseClass(courseClass);
		
		try {
			Quizz savedQuizz = service.saveQuizz(quizz);
			
			return new ResponseEntity<Serializable>(savedQuizz, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
