package com.aprendigame.apredigameapi.api.dto;

import java.util.List;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherDTO {

	private String name;
	private String registration;
	private String password;
	private List<CoursesUnit> courses;
	private List<CourseClass> courseclasses;
}
