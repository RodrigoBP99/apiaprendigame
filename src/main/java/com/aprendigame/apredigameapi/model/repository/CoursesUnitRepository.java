package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

public interface CoursesUnitRepository extends JpaRepository<CoursesUnit, Long>{
	
	boolean existsByCode(String code);
	
	Optional<CoursesUnit> findByCode(String code);

}
