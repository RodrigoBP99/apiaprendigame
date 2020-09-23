package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Student;

public interface StudentService {
	
	Student authenticate(String registration, String senha);
	
	Student saveStudent(Student student);
	
	void validateRegistration(String registration);
}
