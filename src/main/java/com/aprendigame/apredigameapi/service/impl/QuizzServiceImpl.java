package com.aprendigame.apredigameapi.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
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
	@Transactional
	public Quizz saveQuizz(Quizz quizz) {
		validateCode(quizz.getCode());
		return repository.save(quizz);
	}

	@Override
	public void validateCode(String code) {
		boolean exists = repository.existsByCode(code);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um Quizz com esse codigo!");
		}	
	}
		
}
