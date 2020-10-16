package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;

public interface PresencRepository extends JpaRepository<Presenc, Long>{
	
	boolean existsByCodeAndStudent(String code, Student student);
	
	Optional<Presenc> findByCodeAndStudent(String code, Student student);

}
