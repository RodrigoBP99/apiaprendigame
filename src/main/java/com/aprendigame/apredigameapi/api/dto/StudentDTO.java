package com.aprendigame.apredigameapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
	
	private Long id;
	private String name;
	private String registration;
	private String schoolName;
	private String courseUnitCode;
	private String birthday;
	private String password;
	private String photo;

}
