package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="courseClass", schema="aprendigame")
public class CourseClass {
	
	private Long id;
	private List<Student> students;
	private CoursesUnit courseUnit;
	private String status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	public CoursesUnit getCourseUnit() {
		return courseUnit;
	}
	public void setCourseUnit(CoursesUnit courseUnit) {
		this.courseUnit = courseUnit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
