package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.model.repository.TeacherRepository;
import com.aprendigame.apredigameapi.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {
	
	private TeacherRepository repository;
	
	public TeacherServiceImpl(TeacherRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Teacher authenticate(String registration, String password) {
		Optional<Teacher> teacher = repository.findByRegistration(registration);
		
		if(!teacher.isPresent()) {
			throw new AutenticationError("Matricula não encontrada");
		}
		
		if(!teacher.get().getPassword().equals(password)) {
			throw new AutenticationError("Senha invalida");
		}
		
		return teacher.get();
	}

	@Override
	public Teacher saveTeacher(Teacher teacher) {
		validateRegistration(teacher.getRegistration());
		return repository.save(teacher);
	}

	@Override
	public void validateRegistration(String registration) {
		boolean exists = repository.existsByRegistration(registration);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um professor cadastrado com essa matricula");
		}
		
	}

}
