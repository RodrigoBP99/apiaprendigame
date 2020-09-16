package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="courseUnit", schema="aprendigame")
public class CoursesUnit {
	
	private Long id;
	private String name;
	private List<Teacher> teacher;
	private List<CourseClass> courseClasses;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Teacher> getTeacher() {
		return teacher;
	}
	public void setTeacher(List<Teacher> teacher) {
		this.teacher = teacher;
	}
	public List<CourseClass> getCourseClasses() {
		return courseClasses;
	}
	public void setCourseClasses(List<CourseClass> courseClasses) {
		this.courseClasses = courseClasses;
	}
	
	

}
