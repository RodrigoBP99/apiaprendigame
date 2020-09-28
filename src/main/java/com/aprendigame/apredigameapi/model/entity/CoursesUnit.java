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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="courseUnit", schema="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoursesUnit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToMany
	private List<Teacher> teacher;
	@OneToMany(mappedBy = "courseUnit", cascade = CascadeType.ALL)
	private List<CourseClass> courseClasses;
	
}
