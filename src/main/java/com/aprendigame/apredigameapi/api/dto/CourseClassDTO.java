package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.entity.Student;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseClassDTO {

	private String name;
	private String code;
	private List<Student> students;
	private String courseUnitCode;
	private String teacherRegistration;
	private List<Quizz> quizzes;
}
