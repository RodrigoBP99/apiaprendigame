package com.aprendigame.apredigameapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresencDTO {
	
	private String code;
	private Long courseClassId;
	private String date;
	private String hour;
	private Long studentId;

}
