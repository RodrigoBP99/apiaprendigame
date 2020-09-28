package com.aprendigame.apredigameapi.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aprendigame.apredigameapi.model.entity.Student;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StudentRepositoryTest {
	
	@Autowired
	StudentRepository repository;
	
	@Test
	public void shouldVerifyTheExistenceOfARegistration() {
		//cenário
		Student student = new Student();
		student.setName("student");
		student.setRegistration("123456");
		repository.save(student);
		
		//ação/execução
		boolean result = repository.existsByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result).isTrue();
		
	}

}
