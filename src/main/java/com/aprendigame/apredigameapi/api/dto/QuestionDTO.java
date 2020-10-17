package com.aprendigame.apredigameapi.api.dto;

import java.util.List;

import com.aprendigame.apredigameapi.model.entity.Answer;
import com.aprendigame.apredigameapi.model.entity.Quizz;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionDTO {

	private String questionTittle;
	private List<Answer> answers;
	private Quizz quizz;
}
