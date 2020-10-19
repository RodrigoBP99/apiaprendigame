package com.aprendigame.apredigameapi.api.dto;

import java.util.List;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.entity.Teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoursesUnitDTO {

	private String name;
	private String code;
	private List<Teacher> teachers;
	private List<CourseClass> coursesClasses;
	private List<Student> students;
}
