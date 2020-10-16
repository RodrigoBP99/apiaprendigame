package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Quizz;

public interface QuizzService {

	Quizz saveQuizz(Quizz quizz);
	
	void validateCode(String code);
}
