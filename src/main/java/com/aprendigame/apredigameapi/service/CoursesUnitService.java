package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CoursesUnitService {
	
	CoursesUnit authenticate(String code);
	
	CoursesUnit saveCoursesUnit(CoursesUnit coursesUnit);
	
	void validateCode(String code);
	
	Optional<CoursesUnit> findByCode(String code);
	
	CoursesUnit updateCourseUnit(CoursesUnit courseUnit);

}
