package com.aprendigame.apredigameapi.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.repository.QuizzRepository;
import com.aprendigame.apredigameapi.service.QuizzService;

public class QuizzServiceImpl implements QuizzService{
	
	private QuizzRepository repository;
	
	public QuizzServiceImpl(QuizzRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Quizz saveQuizz(Quizz quizz) {
		validateCodeAndCourseClass(quizz.getCode(), quizz.getCourseClass());
		return repository.save(quizz);
	}

	@Override
	public void validateCodeAndCourseClass(String code, CourseClass courseClass) {
		boolean exists = repository.existsByCodeAndCourseClass(code, courseClass);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um Quizz com esse codigo nessa Materia");
		}
	}
		
}
