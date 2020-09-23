package com.aprendigame.apredigameapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.StudentRepository;
import com.aprendigame.apredigameapi.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	private StudentRepository repository;

	@Autowired
	public StudentServiceImpl(StudentRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Student autenticar(String registration, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Student salvarStudent(Student student) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarRegistration(String registration) {
		// TODO Auto-generated method stub
		
	}
	

}
