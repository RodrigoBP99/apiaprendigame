package com.aprendigame.apredigameapi.service;

import java.util.List;
import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CourseClassService {
		
	CourseClass saveCourseClass(CourseClass courseClass);
	
	void validateCodeAndCourseUnit(String code, CoursesUnit courseUnit);
		
	CourseClass updateCourseClass(CourseClass courseClass);
	
	List<CourseClass> search(CourseClass courseClassFiler);
	
	Optional<CourseClass> findById(Long id);
	
	void deleteCourseClass(CourseClass courseClass);
	
}
