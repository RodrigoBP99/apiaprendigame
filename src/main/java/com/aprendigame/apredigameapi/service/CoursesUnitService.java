package com.aprendigame.apredigameapi.service;

import java.util.List;
import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CoursesUnitService {
		
	CoursesUnit saveCoursesUnit(CoursesUnit coursesUnit);
	
	void validateCode(String code);
	
	Optional<CoursesUnit> findById(Long id);
	
	Optional<CoursesUnit> findByCode(String code);
	
	CoursesUnit updateCourseUnit(CoursesUnit courseUnit);

	List<CoursesUnit> search(CoursesUnit courseUnitFilter);
}
