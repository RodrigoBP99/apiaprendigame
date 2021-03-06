package com.aprendigame.apredigameapi.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
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
	public Student updateStudent(Student student) {
		Objects.requireNonNull(student.getId());
		return repository.save(student);
	}

	@Override
	@Transactional
	public Student saveStudent(Student student) {
		validateRegistration(student.getRegistration());
		
		if(student.getRegistration() == null) {
			throw new BusinessRuleException("A matricula não pode ser vazia");
		}
		
		if(student.getPassword() == null){
			throw new BusinessRuleException("A senha não pode ser vazia");
		}
		
		return repository.save(student);
	}

	@Override
	public void validateRegistration(String registration) {
		boolean exists = repository.existsByRegistration(registration);
		
		if(exists) {
			throw new BusinessRuleException("Já existe um estudante cadastrado com esse matricula");
		}
	}
	
	@Override
	public Optional<Student> findByRegistration(String registration){
		return repository.findByRegistration(registration);
	}

	@Override
	public Optional<Student> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Student> search(Student studentFilter) {
		Example<Student> example = Example.of(studentFilter,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

}
