package com.aprendigame.apredigameapi.service.impl;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.PresencRepository;
import com.aprendigame.apredigameapi.service.PresencService;

@Service
public class PresencServiceImpl implements PresencService {
	
	private PresencRepository repository;
	
	public PresencServiceImpl(PresencRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public List<Presenc> search(Presenc presencFilter){
		Example<Presenc> example = Example.of(presencFilter,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public Presenc savePresenc(Presenc presenc) {
		validateCodeAndStudentAndCourseClass(presenc.getCode(), presenc.getStudent(), presenc.getCourseClass());
		return repository.save(presenc);
	}

	@Override
	public void validateCodeAndStudentAndCourseClass(String code, Student student, CourseClass courseClass) {
		boolean exists = repository.existsByCodeAndStudentAndCourseClass(code, student, courseClass);
		
		if(exists) {
			throw new BusinessRuleException("Essa presença já foi registrada");
		}
		
	}

}
