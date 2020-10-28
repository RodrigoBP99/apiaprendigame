package com.aprendigame.apredigameapi.service.impl;

import java.util.Objects;
import java.util.Optional;

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
		Objects.requireNonNull(courseUnit.getCode());
		return repository.save(courseUnit);
	}

	@Override
	public Optional<CoursesUnit> findByCode(String code) {
		return repository.findByCode(code);
	}
}
