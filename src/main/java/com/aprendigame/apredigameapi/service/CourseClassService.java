package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.CourseClass;

public interface CourseClassService {
	
	CourseClass saveCourseClass(CourseClass courseClass);
	
	void validateCode(String code);

}
