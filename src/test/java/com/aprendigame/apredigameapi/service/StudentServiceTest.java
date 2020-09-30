package com.aprendigame.apredigameapi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.repository.StudentRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class StudentServiceTest {

	@Autowired
	StudentService service;
	
	@Autowired
	StudentRepository repository;
	
	@Test
	public void shouldValidateRegistration() {
		
		//cenario
		repository.deleteAll();
		
		//ação
		service.validateRegistration("123456");
	}

	@Test
	public void shouldGiveErrorWhenValidateRegistrationIfThereIsRegiteredRegistrarion() {
		
		//cenario
		Student student = new Student();
		student.setName("student");
		student.setRegistration("123456");
		repository.save(student);
		
		//ação
		assertThrows(BusinessRuleException.class, () -> service.validateRegistration("123456"), "Já existe um estudante cadastrado com esse matricula");
	}
}
