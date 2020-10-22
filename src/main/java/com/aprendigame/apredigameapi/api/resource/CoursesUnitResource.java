package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CoursesUnitDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.service.CoursesUnitService;

@RestController
@RequestMapping("/api/courseUnit")
public class CoursesUnitResource {

	private CoursesUnitService service;
	
	public CoursesUnitResource(CoursesUnitService service) {
		this.service = service;
	}
	
	@PostMapping("/find")
	public ResponseEntity<Serializable> findCourseUnit(@RequestBody CoursesUnitDTO dto) {
		try {
			CoursesUnit authenticatedCoursesUnit = service.authenticate(dto.getCode());
			return ResponseEntity.ok(authenticatedCoursesUnit);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{code}")
	public ResponseEntity update(@PathVariable("code") String code, @RequestBody CoursesUnitDTO dto) {
		return service.findByCode(code).map(entity -> {
			try {
				CoursesUnit courseUnit = convert(dto);
				courseUnit.setId(entity.getId());
				courseUnit.setCode(entity.getCode());
				
				service.updateCourseUnit(courseUnit);
				return ResponseEntity.ok(courseUnit);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Não foi encontrado curso com essse codigo", HttpStatus.BAD_REQUEST));
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> saveCourseClass(@RequestBody CoursesUnitDTO dto) {		
		try {
			CoursesUnit savedCourseUnit = convert(dto);
			savedCourseUnit = service.saveCoursesUnit(savedCourseUnit);
			
			return new ResponseEntity<Serializable>(savedCourseUnit, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
			// TODO: handle exception
		}
		
	}
	
	private CoursesUnit convert(CoursesUnitDTO dto) {
		CoursesUnit courseUnit = new CoursesUnit();
		courseUnit.setCode(dto.getCode());
		courseUnit.setName(dto.getName());
		
		return courseUnit;
	}
}
