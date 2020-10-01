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
	public void shouldVerifyTheExistenceOfAStudentRegistration() {
		//cenário
		Student student = createStudent();
		entityManager.persist(student);
		
		//ação/execução
		boolean result = repository.existsByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result).isTrue();		
	}
	
	@Test
	public void shouldReturnFalseWhenDontExistARegisteredStudent() {
		//ação
		boolean result = repository.existsByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void shouldPersistAStudentInDataBase() {
		//cenario
		Student student = createStudent();
		
		//açao
		Student savedStudent = repository.save(student);
		
		//verificação
		Assertions.assertThat(savedStudent.getId()).isNotNull();
	}
	
	@Test
	public void shouldFindAStudentByRegistration() {
		//cenario
		Student student = createStudent();
		entityManager.persist(student);
		
		//ação
		Optional<Student> result = repository.findByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void shouldReturnEmpityWhenSearchForAStudentThatDontExistOnDataBase() {
		//ação
		Optional<Student> result = repository.findByRegistration("123456");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Student createStudent() {
		Student student = new Student();
		student.setName("student");
		student.setRegistration("123456");
		student.setPassword("147258");
		student.setBirthday("07/04/1999");
		student.setCourse("course");
		student.setSchoolName("school");
		student.setActualLevel(1);
		student.setNextLevel(2);
		student.setPoints(0);
		student.setRequiredPoints(100);
		
		return student;
	}

}
