package com.aprendigame.apredigameapi.model.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
		
	boolean existsByRegistration(String registration);
	
	Optional<Student> findByRegistration(String registration);

}
