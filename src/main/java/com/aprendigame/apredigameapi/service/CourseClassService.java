package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CourseClassService {
	
	CourseClass findCourseClass(String code, CoursesUnit courseUnit);
	
	CourseClass saveCourseClass(CourseClass courseClass);
	
	void validateCodeAndCourseUnit(String code, CoursesUnit courseUnit);

}
