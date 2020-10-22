package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.Teacher;

public interface TeacherService {
	
	Teacher authenticate(String registration, String password);
	
	Teacher saveTeacher(Teacher teacher);
	
	void validateRegistration(String registration);
	
	Optional<Teacher> findByRegistration(String registration);
	
	Teacher updateTeacher(Teacher teacher);
}
