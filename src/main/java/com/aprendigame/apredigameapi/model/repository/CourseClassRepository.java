package com.aprendigame.apredigameapi.model.repository;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long>{
	
	boolean existsByCodeAndCourseUnit(String code, CoursesUnit courseUnit);
	
	Optional<CourseClass> findByCodeAndCourseUnit(String code, CoursesUnit coursesUnit);
		
}
