package com.aprendigame.apredigameapi.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.repository.CourseClassRepository;
import com.aprendigame.apredigameapi.service.CourseClassService;

@Service
public class CourseClassServiceImpl implements CourseClassService{
	
	private CourseClassRepository repository;
	
	public CourseClassServiceImpl(CourseClassRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public CourseClass saveCourseClass(CourseClass courseClass) {
		validateCodeAndCourseUnit(courseClass.getCode(), courseClass.getCourseUnit());
		return repository.save(courseClass);
	}

	@Override
	public void validateCodeAndCourseUnit(String code, CoursesUnit coursesUnit) {
		boolean exists = repository.existsByCodeAndCourseUnit(code, coursesUnit);
		
		if (exists) {
			throw new BusinessRuleException("Já existe uma Matéria com esse Código");
		}
	}

	@Override
	public CourseClass updateCourseClass(CourseClass courseClass) {
		Objects.requireNonNull(courseClass.getId());
		Objects.requireNonNull(courseClass.getCode());
		return repository.save(courseClass);
	}

	@Override
	public List<CourseClass> search(CourseClass courseClassFiler) {
		Example example = Example.of(courseClassFiler,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public Optional<CourseClass> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public void deleteCourseClass(CourseClass courseClass) {
		Objects.requireNonNull(courseClass.getId());
		repository.delete(courseClass);
	}
}
