package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.AutenticationError;
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
	public CourseClass findCourseClass(String code, CoursesUnit coursesUnit) {
		Optional<CourseClass> courseClass = repository.findByCodeAndCourseUnit(code, coursesUnit);
		
		if(!courseClass.isPresent()) {
			throw new AutenticationError("Não foi possivel encontrar uma Matéria com esse código");
		}
		
		if (!courseClass.get().getCourseUnit().equals(coursesUnit)) {
			throw new AutenticationError("Não foi possivel encontrar uma Matéria com esse código Para esse Curso");
		}
		return courseClass.get();
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
}
