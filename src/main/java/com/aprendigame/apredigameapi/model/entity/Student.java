package com.aprendigame.apredigameapi.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "student", schema = "aprendigameapi")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String registration;
	private String password;
	@ManyToOne
	@JoinColumn(name = "courseUnit_id")
	@JsonIgnoreProperties({"teachers", "courseClasses", "students"})
	private CoursesUnit courseUnit;
	private String photo;
	private String schoolName;
	private String birthday;
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Presenc> presences;
	@ManyToMany
	@JoinColumn(name = "courseClass_id")
	@JsonIgnoreProperties({"students", "quizzes"})
	private List<CourseClass> listClass;
	private double points;
	private double requiredPoints;
	private int actualLevel;
	private int nextLevel;
	@ManyToMany
	@JsonIgnoreProperties({"questions"})
	private List<Quizz> answeredQuizz;
	
}
