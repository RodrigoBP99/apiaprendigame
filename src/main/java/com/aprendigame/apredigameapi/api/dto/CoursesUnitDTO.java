package com.aprendigame.apredigameapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursesUnitDTO {

	private String name;
	private String code;
	private String teacherRegistration;
	private String courseClassCode;
	private String studentRegistration;
}
