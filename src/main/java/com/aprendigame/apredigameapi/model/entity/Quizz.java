package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="quizz", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Quizz {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@ManyToOne
	private CourseClass courseClass;
	private String amountOfQuestions;
	@OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL)
	private List<Question> questions;
	

}
