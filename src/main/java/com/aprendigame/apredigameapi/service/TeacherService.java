package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Teacher;

public interface TeacherService {
	
	Teacher authenticate(String registration, String password);
	
	Teacher saveTeacher(Teacher teacher);
	
	void validateRegistration(String registration);
}
