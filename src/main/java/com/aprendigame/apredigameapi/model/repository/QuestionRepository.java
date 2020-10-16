package com.aprendigame.apredigameapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendigame.apredigameapi.model.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
