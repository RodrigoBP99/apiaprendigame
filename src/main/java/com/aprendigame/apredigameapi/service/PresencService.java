package com.aprendigame.apredigameapi.service;

import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;

public interface PresencService {
	
	Presenc savePresenc(Presenc presenc);

	void validateCodeAndStudent(String code, Student student);
}
