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
@Table(name="courseUnit", schema="aprendigame")
@Builder
@Data
public class CoursesUnit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToMany
	private List<Teacher> teacher;
	@OneToMany(mappedBy = "courseUnit", cascade = CascadeType.ALL)
	private List<CourseClass> courseClasses;
	
}
