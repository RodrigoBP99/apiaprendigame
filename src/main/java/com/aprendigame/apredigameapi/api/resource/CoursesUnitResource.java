package com.aprendigame.apredigameapi.api.resource;

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
	private TeacherResource teacherResource;
	
	public CoursesUnitResource(CoursesUnitService service, TeacherService serviceTeacher, TeacherResource teacherResource) {
		this.service = service;
		this.serviceTeacher = serviceTeacher;
		this.teacherResource = teacherResource;
	}
	

	@SuppressWarnings("unused")
	@GetMapping
	public ResponseEntity<Object> findCourseUnit(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "teacherId", required = false) Long teacherId,
			@RequestParam(value = "code", required = false) String code) {
		
		CoursesUnit courseUnitFilter = new CoursesUnit();
		
		courseUnitFilter.setName(name);
		courseUnitFilter.setCode(code);
		
		
		List<CoursesUnit> coursesUnits = service.search(courseUnitFilter);
		List<CoursesUnit> coursesUnits2 = new ArrayList<CoursesUnit>();
		
		if(teacherId != null) {
			Teacher teacher = serviceTeacher.findById(teacherId)
					.orElseThrow(() -> new BusinessRuleException("Não foi econtrado o professor no nosso banco de dados"));
			for (CoursesUnit courseUnit : coursesUnits) {
				for(Teacher teacher1 : courseUnit.getTeachers()) {
					if(teacher1.getId().equals(teacher.getId())) {
						coursesUnits2.add(courseUnit);
					}
				}
			}
			return ResponseEntity.ok(coursesUnits2);
		}
		
		
		return ResponseEntity.ok(coursesUnits);	
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<CoursesUnitDTO> getCourseUnit(@PathVariable("id") Long id) {
		try {
			Optional<CoursesUnit> courseUnit = service.findById(id);
			CoursesUnitDTO coursesUnitDTO = convertDTO(courseUnit.get());
			return new ResponseEntity<CoursesUnitDTO>(coursesUnitDTO, HttpStatus.OK);
		} catch (BusinessRuleException e) {
			return new ResponseEntity<CoursesUnitDTO>(HttpStatus.NOT_FOUND);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@PutMapping("/{id}/includeteacher")
	public ResponseEntity includeTeacher(@PathVariable("id") Long id, @RequestBody CoursesUnitDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit coursesUnit = entity;
				
				Optional<Teacher> teacher = serviceTeacher.findByRegistration(dto.getTeacherRegistration());
				if (!teacher.isPresent()) {
					return ResponseEntity.badRequest().body("Professor não encontrado para a matricula informada");
				} 
				
				List<Teacher> teachers = coursesUnit.getTeachers();
				
				if (teachers.contains(teacher.get())) {
					return ResponseEntity.badRequest().body("Professor já está no Curso");
				}
				
				teachers.add(teacher.get());
				coursesUnit.setTeachers(teachers);
				
				ResponseEntity response = teacherResource.includeTeacherInCourseUnit(teacher.get().getId(), coursesUnit);
				
				if (response.equals(ResponseEntity.ok(teacher.get()))) {
					service.updateCourseUnit(coursesUnit);
					return ResponseEntity.ok(coursesUnit);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@PutMapping("/{id}/removeteacher")
	public ResponseEntity removeTeacher(@PathVariable("id") Long id, @RequestBody CoursesUnitDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				CoursesUnit coursesUnit = entity;
				
				Optional<Teacher> teacher = serviceTeacher.findByRegistration(dto.getTeacherRegistration());
				
				if (!teacher.isPresent()) {
					return ResponseEntity.badRequest().body("Professor não encontrado para a matricula informada");
				} 
				
				List<Teacher> teachers = coursesUnit.getTeachers();
				
				if (teachers.contains(teacher.get())) {
					return ResponseEntity.badRequest().body("Professor já está no Curso");
				}
				
				teachers.remove(teacher.get());
				coursesUnit.setTeachers(teachers);
				
				ResponseEntity response = teacherResource.removeTeacherFromCourseUnit(teacher.get().getId(), coursesUnit);
				
				if (response.equals(ResponseEntity.ok(teacher.get()))) {
					service.updateCourseUnit(coursesUnit);
					return ResponseEntity.ok(coursesUnit);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Curso não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/save")
	public ResponseEntity saveCourseClass(@RequestBody CoursesUnitDTO dto) {		
		try {
			CoursesUnit savedCourseUnit = convert(dto);

			Optional<Teacher> teacher = serviceTeacher.findById(dto.getTeacherId());
			if(teacher.isPresent()) {
				List<Teacher> teachers = new ArrayList<>();
				teachers.add(teacher.get());
				savedCourseUnit.setTeachers(teachers);
			}
			
			savedCourseUnit = service.saveCoursesUnit(savedCourseUnit);
			teacherResource.includeTeacherInCourseUnit(teacher.get().getId(), savedCourseUnit);
			return new ResponseEntity(savedCourseUnit, HttpStatus.CREATED);
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
