package com.aprendigame.apredigameapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

}
