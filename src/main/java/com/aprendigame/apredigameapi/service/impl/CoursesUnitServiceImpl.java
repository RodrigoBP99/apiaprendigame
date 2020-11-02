package com.aprendigame.apredigameapi.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

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
	public CoursesUnit saveCoursesUnit(CoursesUnit coursesUnit) {
		
		if (coursesUnit.getCode() == null) {
			throw new BusinessRuleException("O Código do curso é obrigatorio!");
		}
		
		if (coursesUnit.getName() == null) {
			throw new BusinessRuleException("O nome do curso é obrigatorio!");
		}
		
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
	
	@Override
	public Optional<CoursesUnit> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public CoursesUnit updateCourseUnit(CoursesUnit courseUnit) {
		Objects.requireNonNull(courseUnit.getId());
		return repository.save(courseUnit);
	}

	@Override
	public Optional<CoursesUnit> findByCode(String code) {
		return repository.findByCode(code);
	}

	@Override
	public List<CoursesUnit> search(CoursesUnit courseUnitFilter) {
		Example<CoursesUnit> example = Example.of(courseUnitFilter,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}
}
