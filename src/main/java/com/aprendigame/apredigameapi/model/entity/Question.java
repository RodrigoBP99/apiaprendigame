package com.aprendigame.apredigameapi.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="question", schema="aprendigameapi")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String questionTittle;
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"question"})
	private List<Answer> answers;
	@ManyToOne
	@JoinColumn(name = "quizz_id")
	@JsonIgnoreProperties({"questions", "courseClass"})
	private Quizz quizz;
}
