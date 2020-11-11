package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.StudentDTO;
import com.aprendigame.apredigameapi.exception.AutenticationError;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Quizz;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentResource {
	
	private StudentService service;
	private CoursesUnitService courseUnitService;
	
	public StudentResource(StudentService service, CoursesUnitService courseUnitService) {
		this.service = service;
		this.courseUnitService = courseUnitService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Serializable> authenticate( @RequestBody StudentDTO dto) {
		try {
			Student authenticatedStudent = service.authenticate(dto.getRegistration(), dto.getPassword());
			return ResponseEntity.ok(authenticatedStudent);
		} catch (AutenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("/getStudent/{id}")
	public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
		try {
			Student student = service.findById(id)
					.orElseThrow( () -> new BusinessRuleException("Estudante não encontrado!") );
			return new ResponseEntity<Student>(student, HttpStatus.OK);
		} catch (BusinessRuleException e) {
			return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<Serializable> save( @RequestBody StudentDTO dto) {		
		try {
			Student savedStudent = convert(dto);
			CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
					.orElseThrow(() -> new BusinessRuleException("Curso não encontrado com esse Código"));
		
			savedStudent.setCourseUnit(courseUnit);
			
			savedStudent = service.saveStudent(savedStudent);
			
			return new ResponseEntity<Serializable>(savedStudent, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{id}")
	public ResponseEntity updateStudent(@PathVariable("id") Long id, @RequestBody StudentDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				Student student = convert(dto);
				student.setId(entity.getId());
				student.setRegistration(entity.getRegistration());
				student.setActualLevel(entity.getActualLevel());
				student.setNextLevel(entity.getActualLevel());
				student.setPoints(entity.getPoints());
				student.setRequiredPoints(entity.getRequiredPoints());
				student.setPassword(entity.getPassword());
				student.setListClass(entity.getListClass());
				student.setCourseUnit(entity.getCourseUnit());
				
				
				service.updateStudent(student);
				return ResponseEntity.ok(student);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
				new ResponseEntity("Estudante não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity includeStudentInCourseClass(@PathVariable("id") Long id, @RequestBody CourseClass courseClass) {
		return service.findById(id).map(entity -> {
			try {
				Student student = entity;
				
				List<CourseClass> courseClasses = student.getListClass();
				
				
				if(!courseClasses.contains(courseClass)) {
					courseClasses.add(courseClass);
					
					student.setListClass(courseClasses);
					service.updateStudent(student);
					return ResponseEntity.ok(student);
				} else {
					return ResponseEntity.badRequest().body("Estudante já cadastrado na turma");
				}
				
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Studante não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity removeStudentFromCourseClass(@PathVariable("id") Long id, @RequestBody CourseClass courseClass) {
		return service.findById(id).map(entity -> {
			try {
				Student student = entity;
				
				List<CourseClass> courseClasses = student.getListClass();
				
				
				if(courseClasses.contains(courseClass)) {
					courseClasses.remove(courseClass);
					
					student.setListClass(courseClasses);
					service.updateStudent(student);
					return ResponseEntity.ok(student);
				} else {
					return ResponseEntity.badRequest().body("Estudante não está cadastrado na turma");
				}
				
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Estudante não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/updatePointsAndLevel")
	public ResponseEntity updateStudentPointsAndLevel(@PathVariable("id") Long id, @RequestBody Quizz quizz) {
		return service.findById(id).map(entity -> {
			try {
				Student student = entity;
				
				List<Quizz> listQuizz = entity.getAnsweredQuizz();
				listQuizz.add(quizz);
				student.setAnsweredQuizz(listQuizz);
				
				student = verifyStudentPoints(quizz.getPoints(), entity, student);
				
				service.updateStudent(student);
				return ResponseEntity.ok(student);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
				new ResponseEntity("Estudante não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}

	private Student verifyStudentPoints(Double quizzPoints, Student entity, Student student) {
		Double points = student.getPoints();
		points = points + quizzPoints;
		if(points >= student.getRequiredPoints()) {
			student.setActualLevel(student.getNextLevel());
			student.setNextLevel(entity.getNextLevel()+1);
			Double total = points - student.getRequiredPoints();
			
			student.setRequiredPoints(student.getActualLevel()*25);
			
			if (total >= student.getRequiredPoints()) {
				verifyStudentPoints(total, entity, student);
			} else {
				student.setPoints(total);
			}
		}
		return student;
	}
	
	private Student convert(StudentDTO dto) {
		Student student = new Student();
		student.setName(dto.getName());
		student.setRegistration(dto.getRegistration());
		student.setPhoto(dto.getPhoto());
		student.setSchoolName(dto.getSchoolName());
		student.setBirthday(dto.getBirthday());
		student.setPassword(dto.getPassword());
		student.setActualLevel(1);
		student.setNextLevel(2);
		student.setPoints(0);
		student.setRequiredPoints(student.getActualLevel()*25);
		
		return student;
	}
}
