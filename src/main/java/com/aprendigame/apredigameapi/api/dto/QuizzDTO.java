package com.aprendigame.apredigameapi.api.dto;

import java.util.List;
import com.aprendigame.apredigameapi.model.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizzDTO {
	
	private String code;
	private String title;
	private String courseClassCode;
	private String amountOfQuestions;
	private List<Question> questions;
	private Double points;

}
