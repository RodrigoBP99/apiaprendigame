package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;

public interface QuizzService {
	
	Quizz findQuizz(String code, CourseClass courseClass);

	Quizz saveQuizz(Quizz quizz);
	
	void validateCodeAndCourseClass(String code, CourseClass courseClass);
	
	Optional<Quizz> findById(Long id);
}
