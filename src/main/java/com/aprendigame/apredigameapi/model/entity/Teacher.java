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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="teacher", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Teacher implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String registration;
	private String photo;
	private String password;
	@ManyToMany
	@JoinColumn(name = "courseUnit_id")
	@JsonIgnoreProperties({"teachers", "courseClasses", "students"})
	private List<CoursesUnit> courseUnit;
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"teacher", "quizzes", "students"})
	private List<CourseClass> courseClasses;

}
