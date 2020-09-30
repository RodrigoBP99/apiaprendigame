package com.aprendigame.apredigameapi.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aprendigame.apredigameapi.model.entity.Student;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StudentRepositoryTest {
	
	@Autowired
	StudentRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void shouldVerifyTheExistenceOfARegistration() {
		//cenário
		Student student = new Student();
		student.setName("student");
		student.setRegistration("123456");
		entityManager.persist(student);
		
		//ação/execução
		boolean result = repository.existsByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result).isTrue();		
	}
	
	@Test
	public void shouldReturnFalseWhenDontExistARegistredStudent() {
		//cenário
		
		//ação
		boolean result = repository.existsByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}

}
