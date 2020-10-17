package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CoursesUnitService {
	
	CoursesUnit authenticate(String code);
	
	CoursesUnit saveCoursesUnit(CoursesUnit coursesUnit);
	
	void validateCode(String code);

}
