package com.aprendigame.apredigameapi.api.dto;

import java.util.List;

import com.aprendigame.apredigameapi.model.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

	private String questionTittle;
	private List<Answer> answers;
	private Long quizzId;
}
