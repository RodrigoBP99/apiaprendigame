package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.AnswerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

	private String text;
	private AnswerType answerType;
	private Long questionId;
}
