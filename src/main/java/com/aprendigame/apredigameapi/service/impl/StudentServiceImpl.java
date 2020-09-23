package com.aprendigame.apredigameapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.StudentRepository;
import com.aprendigame.apredigameapi.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	private StudentRepository repository;

	public StudentServiceImpl(StudentRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Student authenticate(String registration, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Student saveStudent(Student student) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateRegistration(String registration) {
		boolean exists = repository.existsByRegistration(registration);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um estudante cadastrado com esse matricula");
		}
	}
	

}
