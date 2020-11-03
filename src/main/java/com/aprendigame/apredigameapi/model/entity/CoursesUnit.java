package com.aprendigame.apredigameapi.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="courseUnit", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CoursesUnit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	@ManyToMany
	@JsonIgnoreProperties({"courseUnit", "password" , "courseClasses"})
	private List<Teacher> teachers;
	@OneToMany(mappedBy = "courseUnit", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"students", "courseUnit", "quizzes"})
	private List<CourseClass> courseClasses;
	@OneToMany(mappedBy = "courseUnit")
	@JsonIgnoreProperties({"presences", "listClass", "courseUnit", "password"})
	private List<Student> students;
	
}
