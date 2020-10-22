package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CourseClassService {
	
	CourseClass findCourseClass(String code, CoursesUnit courseUnit);
	
	CourseClass saveCourseClass(CourseClass courseClass);
	
	void validateCodeAndCourseUnit(String code, CoursesUnit courseUnit);
	
	Optional<CourseClass> findByCode(String code);
	
	CourseClass updateCourseClass(CourseClass courseClass);
	
}
