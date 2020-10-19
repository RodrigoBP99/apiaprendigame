package com.aprendigame.apredigameapi.api.dto;

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
	private String studentRegistration;

}
