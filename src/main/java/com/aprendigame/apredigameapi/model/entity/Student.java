package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student", schema = "aprendigame")
public class Student {
	
	private Long id;
	private String name;
	private int password;
	private String courseName;
	private String photo;
	private String schoolName;
	private String birthday;
	private String details;
	private String course;
	private List<Presenc> presences;
	private List<CourseClass> listClass;
	private double points;
	private double requiredPoints;
	private int actualLevel;
	private int nextLevel;
	
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
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public List<Presenc> getPresences() {
		return presences;
	}
	public void setPresences(List<Presenc> presences) {
		this.presences = presences;
	}
	public List<CourseClass> getListClass() {
		return listClass;
	}
	public void setListClass(List<CourseClass> listClass) {
		this.listClass = listClass;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public double getRequiredPoints() {
		return requiredPoints;
	}
	public void setRequiredPoints(double requiredPoints) {
		this.requiredPoints = requiredPoints;
	}
	public int getActualLevel() {
		return actualLevel;
	}
	public void setActualLevel(int actualLevel) {
		this.actualLevel = actualLevel;
	}
	public int getNextLevel() {
		return nextLevel;
	}
	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	
}
