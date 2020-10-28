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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="courseClass", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseClass implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	@ManyToMany
	@JoinColumn(name = "student_id")
	private List<Student> students;
	@ManyToOne
	@JoinColumn(name = "courseUnit_id")
	private CoursesUnit courseUnit;
	@ManyToOne
	private Teacher teacher;
	@OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL)
	private List<Quizz> quizzes;

}