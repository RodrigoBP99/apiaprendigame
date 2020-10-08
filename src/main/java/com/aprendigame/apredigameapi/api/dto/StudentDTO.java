package com.aprendigame.apredigameapi.api.dto;

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
	private String courseName;
	private String birthday;
	private String password;

}
