package com.aprendigame.apredigameapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.PresencRepository;
import com.aprendigame.apredigameapi.service.PresencService;

@Service
public class PresencServiceImpl implements PresencService {
	
	private PresencRepository repository;
	
	public PresencServiceImpl(PresencRepository repository) {
		super();
		this.repository = repository;
	}
	

	@Override
	public Presenc authenticate(String code, Student student) {
		Optional<Presenc> presenc = repository.findByCodeAndStudent(code, student);
		
		if(!presenc.isPresent()) {
			throw new AutenticationError("Não foi possivel encontrar nenhuma Presença com esses dados");
		}
		if(!presenc.get().getCode().equals(code)) {
			throw new AutenticationError("Não foi possivel encontrar nenhuma Presença com esse codigo");
		}
		if(!presenc.get().getStudent().getRegistration().equals(student.getRegistration())) {
			throw new AutenticationError("Não foi possivel encontrar nenhuma Presença para esse estudante");
		}
		return presenc.get();
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
