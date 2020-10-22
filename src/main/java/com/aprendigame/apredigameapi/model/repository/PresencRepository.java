package com.aprendigame.apredigameapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;

public interface PresencRepository extends JpaRepository<Presenc, Long>{
	
	boolean existsByCodeAndStudentAndCourseClass(String code, Student student, CourseClass courseClass);
	
	Optional<Presenc> findByCodeAndStudent(String code, Student student);

}
