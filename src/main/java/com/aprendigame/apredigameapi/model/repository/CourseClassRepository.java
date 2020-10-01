package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.CourseClass;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long>{
	
	boolean existsByCode(String code);
	
	Optional<CourseClass> findByCode(String code);

}
