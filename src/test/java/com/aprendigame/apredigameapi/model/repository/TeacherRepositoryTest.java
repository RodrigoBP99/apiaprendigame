package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

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

import com.aprendigame.apredigameapi.model.entity.Teacher;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TeacherRepositoryTest {
	
	@Autowired
	TeacherRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void shouldVerifyTheExistenceOfTeacherRegistration() {
		//cenario
		Teacher teacher = createTeacher();
		entityManager.persist(teacher);
		
		//ação
		boolean result = repository.existsByRegistration("registration");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void shouldReturnFalseWhenDontExistARegisteredTeacher() {
		//açao
		boolean result = repository.existsByRegistration("registration");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void shouldPersistATeacherInDataBase() {
		//cenario
		Teacher teacher = new Teacher();
		
		//ação
		Teacher savedTeacher = repository.save(teacher);
		
		//verificação
		Assertions.assertThat(savedTeacher.getId()).isNotNull();
	}
	
	@Test
	public void shouldFindATeacherByRegistration() {
		//cenario
		Teacher teacher = createTeacher();
		entityManager.persist(teacher);
		
		//ação
		Optional<Teacher> result = repository.findByRegistration("registration");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void shouldReturnEmpityWhenSearchForATeacherThatDontExistOnDataBase() {
		//ação
		Optional<Teacher> result = repository.findByRegistration("registration");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Teacher createTeacher() {
		Teacher teacher = new Teacher();
		teacher.setName("teacher");
		teacher.setRegistration("registration");
		teacher.setPassword("password");
		
		return teacher;
	}

}
