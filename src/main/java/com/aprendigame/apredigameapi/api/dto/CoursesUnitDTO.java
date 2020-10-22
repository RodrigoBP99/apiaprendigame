package com.aprendigame.apredigameapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoursesUnitDTO {

	private String name;
	private String code;
	private String teacherRegistration;
	private String courseClassCode;
	private String studentRegistration;
}
