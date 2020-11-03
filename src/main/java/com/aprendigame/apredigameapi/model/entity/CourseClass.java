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
@Table(name="courseClass", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CourseClass implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	@ManyToMany
	@JsonIgnoreProperties({"presences", "listClass"})
	private List<Student> students;
	@ManyToOne
	@JoinColumn(name = "courseUnit_id")
	@JsonIgnoreProperties({"teachers", "courseClasses", "students"})
	private CoursesUnit courseUnit;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	@JsonIgnoreProperties({"courseUnit", "courseClasses", "password"})
	private Teacher teacher;
	@OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"questions", "courseClass"})
	private List<Quizz> quizzes;

}