package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.model.entity.Answer;
import com.aprendigame.apredigameapi.model.repository.AnswerRepository;
import com.aprendigame.apredigameapi.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService{

	private AnswerRepository repository;
	
	public AnswerServiceImpl(AnswerRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Answer saveAnswer(Answer answer) {
		return repository.save(answer);
	}

	@Override
	public Optional<Answer> findById(Long id) {
		return repository.findById(id);
	}

}
