package com.aprendigame.apredigameapi.api.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentResource {
	
	@GetMapping("/")
	public String helloWorld() {
		return "hello world";
	}

}
