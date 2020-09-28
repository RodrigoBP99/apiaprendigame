package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name="teacher", schema="aprendigame")
@Builder
@Data
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String registration;
	private String password;
	@ManyToMany
	private List<CoursesUnit> courses;
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<CourseClass> courseclasses;

}
