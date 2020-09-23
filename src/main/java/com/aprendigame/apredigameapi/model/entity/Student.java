package com.aprendigame.apredigameapi.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student", schema = "aprendigame")
public class Student {
	
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
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + actualLevel;
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nextLevel;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		long temp;
		temp = Double.doubleToLongBits(points);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((registration == null) ? 0 : registration.hashCode());
		temp = Double.doubleToLongBits(requiredPoints);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (actualLevel != other.actualLevel)
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nextLevel != other.nextLevel)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (Double.doubleToLongBits(points) != Double.doubleToLongBits(other.points))
			return false;
		if (registration == null) {
			if (other.registration != null)
				return false;
		} else if (!registration.equals(other.registration))
			return false;
		if (Double.doubleToLongBits(requiredPoints) != Double.doubleToLongBits(other.requiredPoints))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", registration=" + registration + ", password=" + password
				+ ", courseName=" + courseName + ", photo=" + photo + ", schoolName=" + schoolName + ", birthday="
				+ birthday + ", details=" + details + ", course=" + course + ", presences=" + presences + ", listClass="
				+ listClass + ", points=" + points + ", requiredPoints=" + requiredPoints + ", actualLevel="
				+ actualLevel + ", nextLevel=" + nextLevel + "]";
	}
}
