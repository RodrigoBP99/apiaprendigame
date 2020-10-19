package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.Question;

public interface QuestionService {
	
	Question saveQuestion(Question question);
	
	Optional<Question> findById(Long id);
}
