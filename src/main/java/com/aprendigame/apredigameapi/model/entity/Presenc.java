package com.aprendigame.apredigameapi.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "presenc", schema ="aprendigameapi")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Presenc {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private String id;
	private String code;
	private String courseClassCode;
	private String date;
	private String hour;
	@ManyToOne
	private Student student;

}
