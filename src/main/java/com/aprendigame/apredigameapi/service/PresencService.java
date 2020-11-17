package com.aprendigame.apredigameapi.service;

import java.util.List;
import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;

public interface PresencService {
	
	Presenc savePresenc(Presenc presenc);

	void validateCodeAndStudentAndCourseClass(String code, Student student, CourseClass courseClass);
	
	List<Presenc> search(Presenc presencFilter);
	
	Optional<Presenc> findById(Long id);
	
	void deletePresenc(Presenc presenc);
}
