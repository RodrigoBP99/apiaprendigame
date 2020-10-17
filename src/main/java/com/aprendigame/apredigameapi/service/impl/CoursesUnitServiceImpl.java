package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.repository.CoursesUnitRepository;
import com.aprendigame.apredigameapi.service.CoursesUnitService;

@Service
public class CoursesUnitServiceImpl implements CoursesUnitService{
	
	private CoursesUnitRepository repository;
	
	public CoursesUnitServiceImpl(CoursesUnitRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public CoursesUnit authenticate(String code) {
		Optional<CoursesUnit> coursesUnit = repository.findByCode(code);
		
		if (!coursesUnit.isPresent()) {
			throw new AutenticationError("Não foi possivel encontrar nenhum Curso com esse código");
		}
		
		return coursesUnit.get();
	}

	@Override
	public CoursesUnit saveCoursesUnit(CoursesUnit coursesUnit) {
		validateCode(coursesUnit.getCode());
		return repository.save(coursesUnit);
	}

	@Override
	public void validateCode(String code) {
		boolean exists = repository.existsByCode(code);
		
		if (exists) {
			throw new BusinessRuleException("Já existe um Curso com esse Código");
		}
	}
}
