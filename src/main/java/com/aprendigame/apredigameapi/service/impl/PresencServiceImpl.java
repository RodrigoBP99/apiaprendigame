package com.aprendigame.apredigameapi.service.impl;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.PresencRepository;
import com.aprendigame.apredigameapi.service.PresencService;

public class PresencServiceImpl implements PresencService {
	
	private PresencRepository repository;
	
	public PresencServiceImpl(PresencRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Presenc savePresenc(Presenc presenc) {
		validateCodeAndStudent(presenc.getCode(), presenc.getStudent());
		return repository.save(presenc);
	}

	@Override
	public void validateCodeAndStudent(String code, Student student) {
		boolean exists = repository.existsByCodeAndStudent(code, student);
		
		if(exists) {
			throw new BusinessRuleException("Essa presença já foi registrada");
		}
		
	}

}
