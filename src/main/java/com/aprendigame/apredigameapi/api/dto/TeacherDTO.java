package com.aprendigame.apredigameapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
