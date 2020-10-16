package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Quizz;

public interface QuizzRepository extends JpaRepository<Quizz, Long>{
	
	boolean existsByCodeAndCourseClass(String code, CourseClass courseClass);
	
	Optional<Quizz> findByCodeAndCourseClass(String code, CourseClass courseClass);

}
