package com.aprendigame.apredigameapi.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="answer", schema="aprendigameapi")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Answer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String text;
	@Enumerated(value = EnumType.STRING)
	private AnswerType answerType;
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	

}
