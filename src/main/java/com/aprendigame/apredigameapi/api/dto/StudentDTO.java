package com.aprendigame.apredigameapi.api.dto;

import com.aprendigame.apredigameapi.model.entity.CoursesUnit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentDTO {
	
	private String name;
	private String registration;
	private String schoolName;
	private CoursesUnit courseUnit;
	private String birthday;
	private String password;
	private String photo;

}
