package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.Student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PresencDTO {
	
	private String code;
	private String courseClassCode;
	private String date;
	private String hour;
	private Student student;

}
