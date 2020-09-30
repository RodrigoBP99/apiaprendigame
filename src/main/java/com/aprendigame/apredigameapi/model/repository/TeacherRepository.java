package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>{
	
	boolean existsByRegistration(String registration);
	
	Optional<Teacher> findByRegistration(String registration);

}
