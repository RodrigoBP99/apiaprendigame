package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.AnswerType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDTO {

	private String text;
	private AnswerType answerType;
	private Long questionId;
}
