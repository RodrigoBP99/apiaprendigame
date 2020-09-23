package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Student;

public interface StudentService {
	
	Student autenticar(String registration, String senha);
	
	Student salvarStudent(Student student);
	
	void validarRegistration(String registration);
}
