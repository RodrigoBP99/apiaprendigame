package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.repository.QuizzRepository;
import com.aprendigame.apredigameapi.service.QuizzService;

@Service
public class QuizzServiceImpl implements QuizzService{
	
	private QuizzRepository repository;
	
	public QuizzServiceImpl(QuizzRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
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

	@Override
	public Quizz findQuizz(String code, CourseClass courseClass) {
		Optional<Quizz> quizz = repository.findByCodeAndCourseClass(code, courseClass);
		
		if(!quizz.isPresent()) {
			throw new AutenticationError("Não Foi possivel encontrar um Quizz com esse Código");
		}
		
		if(!quizz.get().getCourseClass().equals(courseClass)) {
			throw new AutenticationError("Não existe Quizz com esse Código nessa Aula");
		}
		return quizz.get();
	}

	@Override
	public Optional<Quizz> findById(Long id) {
		return repository.findById(id);
	}
		
}
