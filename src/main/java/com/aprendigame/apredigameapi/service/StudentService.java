package com.aprendigame.apredigameapi.service;

import java.util.List;
import java.util.Optional;

import com.aprendigame.apredigameapi.model.entity.Student;

public interface StudentService {
	
	Student authenticate(String registration, String password);
	
	Student saveStudent(Student student);
	
	Student updateStudent(Student student);
	
	void validateRegistration(String registration);
	
	Optional<Student> findByRegistration(String registration);
	
	Optional<Student> findById(Long id);
	
	List<Student> search(Student studentFilter);
}
