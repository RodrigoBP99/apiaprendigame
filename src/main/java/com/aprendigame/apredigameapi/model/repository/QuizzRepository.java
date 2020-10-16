package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Quizz;

public interface QuizzRepository extends JpaRepository<Quizz, Long>{
	
	boolean existsByCode(String code);
	
	Optional<Quizz> findByCode(String code);

}
