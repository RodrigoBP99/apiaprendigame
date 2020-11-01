package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.entity.Student;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClassDTO {

	private Long id;
	private String name;
	private String code;
	private String studentRegistration;
	private String courseUnitCode;
	private Long teacherId;
	private String teacherRegistration;
	private List<Quizz> quizzes;
	private List<Student> students;
}
