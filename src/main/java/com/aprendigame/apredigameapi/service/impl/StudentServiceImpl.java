package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprendigame.apredigameapi.exception.AutenticationError;
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
	public Student authenticate(String registration, String password) {
		Optional<Student> student = repository.findByRegistration(registration);
		
		if(!student.isPresent()) {
			throw new AutenticationError("Não foi encontrado Estudantes com essa Matricula");
		}
		
		if(!student.get().getPassword().equals(password)) {
			throw new AutenticationError("Senha inválida!");
		}
		
		return student.get();
	}

	@Override
	@Transactional
	public Student saveStudent(Student student) {
		validateRegistration(student.getRegistration());
		return repository.save(student);
	}

	@Override
	public void validateRegistration(String registration) {
		boolean exists = repository.existsByRegistration(registration);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um estudante cadastrado com esse matricula");
		}
	}
	

}
