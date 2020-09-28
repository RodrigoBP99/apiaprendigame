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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "student", schema = "aprendigameapi")
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
	private String courseName;
	private String photo;
	private String schoolName;
	private String birthday;
	private String details;
	private String course;
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Presenc> presences;
	@ManyToMany
	@JoinColumn(name = "student_id")
	private List<CourseClass> listClass;
	private double points;
	private double requiredPoints;
	private int actualLevel;
	private int nextLevel;
	
}
