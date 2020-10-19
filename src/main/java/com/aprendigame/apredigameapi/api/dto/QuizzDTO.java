package com.aprendigame.apredigameapi.api.dto;

import java.util.List;

import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Question;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizzDTO {
	
	private String code;
	private String title;
	private Long courseClassId;
	private String amountOfQuestions;
	private List<Question> questions;
	private Double points;

}
