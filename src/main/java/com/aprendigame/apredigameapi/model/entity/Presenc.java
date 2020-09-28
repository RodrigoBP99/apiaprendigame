package com.aprendigame.apredigameapi.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name= "presenc", schema ="aprendigame")
@Builder
@Data
public class Presenc {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private String id;
	private String date;
	@ManyToOne
	private Student student;

}
