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

import com.aprendigame.apredigameapi.api.dto.TeacherDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherResource {

	private TeacherService service;
	private CoursesUnitService serviceCourseUnit;
	
	public TeacherResource(TeacherService service, CoursesUnitService serviceCourseUnit) {
		this.service = service;
		this.serviceCourseUnit = serviceCourseUnit;
	}
	
	
	@GetMapping
	public ResponseEntity<Object> findTeacher(
			@RequestParam(value = "name", required = false)String name,
			@RequestParam(value = "registration", required = false)String registration,
			@RequestParam(value = "courseUnitId", required = false)Long courseUnitId) {
		Teacher teacherFilter = new Teacher();
		
		teacherFilter.setName(name);
		teacherFilter.setRegistration(registration);
		
		List<Teacher> teacherList = service.search(teacherFilter);
		List<Teacher> teacherListFiltred = new ArrayList<Teacher>();
		
		if(courseUnitId != null) {
			CoursesUnit coursesUnit = serviceCourseUnit.findById(courseUnitId)
					.orElseThrow(() -> new BusinessRuleException("Não foi possivel encontrar esse curso"));
			for (Teacher teacher : teacherList) {
				for(CoursesUnit courseUnit1 : teacher.getCourseUnit()) {
					if(courseUnit1.getId().equals(coursesUnit.getId())) {
						teacherListFiltred.add(teacher);
					}
				}
			}
			return ResponseEntity.ok(teacherListFiltred);
		}
		return ResponseEntity.ok(teacherList);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Serializable> authenticate(@RequestBody TeacherDTO dto){
		try {
			Teacher authenticatedTeacher = service.authenticate(dto.getRegistration(), dto.getPassword());
			return ResponseEntity.ok(authenticatedTeacher);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{registration}")
	public ResponseEntity update(@PathVariable("registration") String registration, @RequestBody TeacherDTO dto) {
		return service.findByRegistration(registration).map(entity -> {
			try {
				Teacher teacher = convert(dto);
				teacher.setId(entity.getId());
				teacher.setRegistration(entity.getRegistration());
				
				service.updateTeacher(teacher);
				return ResponseEntity.ok(teacher);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Professor não encontrado para essa matricula", HttpStatus.BAD_REQUEST));
	}
	
	@PostMapping("/register")
	public ResponseEntity<Serializable> save(@RequestBody TeacherDTO dto){
		try {
			Teacher savedTeacher = convert(dto);
			savedTeacher = service.saveTeacher(savedTeacher);
			
			return new ResponseEntity<Serializable>(savedTeacher, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity includeTeacherInCourseUnit(@PathVariable("id") Long id, @RequestBody CoursesUnit courseUnit) {
		return service.findById(id).map(entity -> {
			try {
				Teacher teacher = entity;
				
				List<CoursesUnit> courseUnitList = teacher.getCourseUnit();
				
				if(!courseUnitList.contains(courseUnit)) {
					courseUnitList.add(courseUnit);
					
					service.updateTeacher(teacher);
					return ResponseEntity.ok(teacher);
				} else {
					return ResponseEntity.badRequest().body("O professor já está cadastrado nesse Curso");
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Professor não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ResponseEntity removeTeacherFromCourseUnit(@PathVariable("id") Long id, @RequestBody CoursesUnit coursesUnit) {
		return service.findById(id).map(entity -> {
			try {
				Teacher teacher = entity;
				
				List<CoursesUnit> coursesUnits = teacher.getCourseUnit();
				
				if (coursesUnits.contains(coursesUnit)) {
					coursesUnits.remove(coursesUnit);
					
					teacher.setCourseUnit(coursesUnits);
					service.updateTeacher(teacher);
					return ResponseEntity.ok(teacher);
				} else {
					return ResponseEntity.badRequest().body("O professor parece já não fazer parte desse curso");
				}
			
				
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Professor não encontrado", HttpStatus.BAD_REQUEST));
	}
	

	private Teacher convert(TeacherDTO dto) {
		Teacher teacher = new Teacher();
		teacher.setName(dto.getName());
		teacher.setRegistration(dto.getRegistration());
		teacher.setPassword(dto.getPassword());
		teacher.setPhoto(dto.getPhoto());
		
		Optional<CoursesUnit> courseUnit = serviceCourseUnit.findByCode(dto.getCourseUnitCode());
		
		if(courseUnit.isPresent()) {
			List<CoursesUnit> coursesUnit = teacher.getCourseUnit();
			coursesUnit.add(courseUnit.get());
			teacher.setCourseUnit(coursesUnit);
		}
	
		return teacher;
	}
}
