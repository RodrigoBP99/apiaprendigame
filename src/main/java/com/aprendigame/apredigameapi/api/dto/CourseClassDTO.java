package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.entity.Teacher;

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
	private CoursesUnit courseUnit;
	private Teacher teacher;
	private List<Quizz> quizzes;
}
