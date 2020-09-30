package com.aprendigame.apredigameapi.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.Student;
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
	
	@Test(expected = Test.None.class)
	public void shouldAuthenticateStudentWithSucess() {
		//cenario
		String registration = "123456";
		String password = "147258";
		Student student = new Student();
		student.setRegistration(registration);
		student.setPassword(password);
		student.setId(1L);
		
		Mockito.when(repository.findByRegistration(registration)).thenReturn(Optional.of(student));
		
		//ação
		Student result = service.authenticate(registration, password);
		
		//verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test(expected = Test.None.class)
	public void shouldValidateRegistration() {
		//cenario
		Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validateRegistration("123456");
	}

	@Test(expected = BusinessRuleException.class)
	public void shouldGiveErrorWhenValidateRegistrationIfThereIsRegiteredRegistrarion() {
		//cenario
		Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(true);
		
		//ação
		service.validateRegistration("123456");
	}
	
}
