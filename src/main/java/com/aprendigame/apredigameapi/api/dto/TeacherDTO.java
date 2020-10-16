package com.aprendigame.apredigameapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherDTO {

	private String name;
	private String registration;
	private String password;
}
