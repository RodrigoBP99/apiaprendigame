package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CoursesUnitDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/courseUnit")
public class CoursesUnitResource {

	private CoursesUnitService service;
	private TeacherService serviceTeacher;
	
	public CoursesUnitResource(CoursesUnitService service, TeacherService serviceTeacher) {
		this.service = service;
		this.serviceTeacher = serviceTeacher;
	}
	

	@GetMapping
	public ResponseEntity<Object> findCourseUnit(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "teacherId", required = false) Long teacherId,
			@RequestParam(value = "code", required = false) String code) {
		
		CoursesUnit courseUnitFilter = new CoursesUnit();
		
		courseUnitFilter.setName(name);
		courseUnitFilter.setCode(code);
		
		
		List<CoursesUnit> coursesUnits = service.search(courseUnitFilter);
		
		
		Optional<Teacher> teacher = serviceTeacher.findById(teacherId);
		List<Teacher> teachers = null;
		if (teacher.isPresent()) {
			for (CoursesUnit courseUnit : coursesUnits) {
				teachers = courseUnit.getTeachers();
				if(teachers.isEmpty() || !teachers.contains(teacher.get())) {
					coursesUnits.remove(courseUnit);
				}
				if (coursesUnits.isEmpty()) {
					return ResponseEntity.ok(coursesUnits);
				}
			}
		}
		
		
		return ResponseEntity.ok(coursesUnits);	
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity getCourseUnit(@PathVariable("id") Long id) {
		try {
			Optional<CoursesUnit> courseUnit = service.findById(id);
			CoursesUnitDTO coursesUnitDTO = convertDTO(courseUnit.get());
			return new ResponseEntity(coursesUnitDTO, HttpStatus.OK);
		} catch (BusinessRuleException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	private CoursesUnitDTO convertDTO(CoursesUnit coursesUnit) {
		CoursesUnitDTO dto = new CoursesUnitDTO();
		
		dto.setId(coursesUnit.getId());
		dto.setCode(coursesUnit.getCode());
		dto.setName(coursesUnit.getName());
		
		return dto;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id") Long id, @RequestBody CoursesUnitDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit courseUnit = convert(dto);
				courseUnit.setId(entity.getId());
				
				if (entity.getTeachers() != null || !entity.getTeachers().isEmpty()) {
					courseUnit.setTeachers(entity.getTeachers());
				}
				
				if (entity.getStudents() != null || !entity.getStudents().isEmpty()) {
					courseUnit.setStudents(entity.getStudents());
				}
				courseUnit.setCourseClasses(entity.getCourseClasses());
				
				service.updateCourseUnit(courseUnit);
				return ResponseEntity.ok(courseUnit);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Não foi possivel encontra o curso", HttpStatus.BAD_REQUEST));
	}
	
	@PostMapping("/save")
	public ResponseEntity<Serializable> saveCourseClass(@RequestBody CoursesUnitDTO dto) {		
		try {
			CoursesUnit savedCourseUnit = convert(dto);
			savedCourseUnit = service.saveCoursesUnit(savedCourseUnit);
			
			return new ResponseEntity<Serializable>(savedCourseUnit, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	private CoursesUnit convert(CoursesUnitDTO dto) {
		CoursesUnit courseUnit = new CoursesUnit();
		courseUnit.setCode(dto.getCode());
		courseUnit.setName(dto.getName());
		
		return courseUnit;
	}
}
