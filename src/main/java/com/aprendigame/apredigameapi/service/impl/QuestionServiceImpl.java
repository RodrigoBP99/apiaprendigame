package com.aprendigame.apredigameapi.service.impl;

import com.aprendigame.apredigameapi.model.entity.Question;
import com.aprendigame.apredigameapi.model.repository.QuestionRepository;
import com.aprendigame.apredigameapi.service.QuestionService;

public class QuestionServiceImpl implements QuestionService {
	
	private QuestionRepository repository;
	
	public QuestionServiceImpl(QuestionRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Question saveQuestion(Question question) {
		return repository.save(question);
	}

}
