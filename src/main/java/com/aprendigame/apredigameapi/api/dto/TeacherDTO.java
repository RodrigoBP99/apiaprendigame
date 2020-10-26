package com.aprendigame.apredigameapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

	private String name;
	private String registration;
	private String password;
	private String photo;
	private String courseUnitCode;
	private String courseclasseCode;
}
