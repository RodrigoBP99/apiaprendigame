package com.aprendigame.apredigameapi.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "presenc", schema ="aprendigame")
public class Presenc {
	
	private String id;
	private String date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
