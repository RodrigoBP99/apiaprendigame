package com.aprendigame.apredigameapi.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "presenc", schema ="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Presenc implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private String id;
	private String code;
	@JsonIgnoreProperties({"students", "quizzes"})
	private CourseClass courseClass;
	private String date;
	private String hour;
	@ManyToOne
	@JoinColumn(name = "student_id")
	@JsonIgnoreProperties({"password", "presences", "listClass"})
	private Student student;

}
