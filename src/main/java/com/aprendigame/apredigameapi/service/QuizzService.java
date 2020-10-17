package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;

public interface QuizzService {
	
	Quizz findQuizz(String code, CourseClass courseClass);

	Quizz saveQuizz(Quizz quizz);
	
	void validateCodeAndCourseClass(String code, CourseClass courseClass);
}
