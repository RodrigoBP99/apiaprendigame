package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="question", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String questionTittle;
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers;

}
