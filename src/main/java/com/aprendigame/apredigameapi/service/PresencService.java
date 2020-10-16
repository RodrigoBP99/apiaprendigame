package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;

public interface PresencService {
	
	Presenc authenticate(String code, Student student);
	
	Presenc savePresenc(Presenc presenc);

	void validateCodeAndStudent(String code, Student student);
}
