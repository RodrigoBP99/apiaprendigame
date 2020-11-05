package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.Answer;

public interface AnswerService {

	Answer saveAnswer(Answer answer);
	
	Optional<Answer> findById(Long id);
}
