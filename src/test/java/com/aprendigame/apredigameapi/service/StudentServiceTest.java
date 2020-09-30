package com.aprendigame.apredigameapi.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.aprendigame.apredigameapi.model.repository.StudentRepository;
import com.aprendigame.apredigameapi.service.impl.StudentServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class StudentServiceTest {

	@MockBean
	StudentService service;
	
	@MockBean
	StudentRepository repository;
	
	@Before
	public void setUp() {
		repository = Mockito.mock(StudentRepository.class);
		service = new StudentServiceImpl(repository);
	}
	
	@Test
	public void shouldValidateRegistration() {
		//cenario
		Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validateRegistration("123456");
	}

	@Test
	public void shouldGiveErrorWhenValidateRegistrationIfThereIsRegiteredRegistrarion() {
		//cenario
		Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(true);
		
		//ação
		service.validateRegistration("123456");
	}
}
