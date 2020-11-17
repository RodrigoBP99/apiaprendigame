package com.aprendigame.apredigameapi.api.resource;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.PresencDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.Presenc;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.service.CourseClassService;
import com.aprendigame.apredigameapi.service.PresencService;
import com.aprendigame.apredigameapi.service.StudentService;

@RestController
@RequestMapping("/api/presenc")
public class PresencResource {

	private PresencService service;
	private StudentService studentService;
	private CourseClassService courseClassService;
	
	public PresencResource(PresencService service, StudentService studentService, CourseClassService courseClassService) {
		this.service = service;
		this.studentService = studentService;
		this.courseClassService = courseClassService;
	}
	
	@GetMapping
	public ResponseEntity<Object> findPresenc(
			@RequestParam(value = "code", required = false)String code,
			@RequestParam(value = "studentRegistration", required = false)String studentRegistration,
			@RequestParam(value = "courseClassId", required = false)Long courseClassId) {
		
		Presenc presencFilter = new Presenc();
		
		presencFilter.setCode(code);
		
		if(studentRegistration != null ) {
			Student student = studentService.findByRegistration(studentRegistration)
					.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para a matricula informada"));
			
			presencFilter.setStudent(student);
		}
		
		if(courseClassId != null) {
			CourseClass courseClass = courseClassService.findById(courseClassId)
					.orElseThrow(() -> new BusinessRuleException("Turma não encontrada"));
			
			presencFilter.setCourseClass(courseClass);
		}
		
		List<Presenc> presencs = service.search(presencFilter);
		
		return ResponseEntity.ok(presencs);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.findById(id).map(entity -> {
			
			service.deletePresenc(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
		new ResponseEntity("Presença não encontrada", HttpStatus.NOT_FOUND));
	}
		
	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody PresencDTO dto){		
		try {
			Presenc savedPresenc = convert(dto);
			
			Optional<Student> student = studentService.findById(dto.getStudentId());
			if (!student.isPresent()) {
				return ResponseEntity.badRequest().body("Estudante não Encontrado na base de Dados");
			}
			
			Optional<CourseClass> courseClass = courseClassService.findById(dto.getCourseClassId());
			
			if (!courseClass.isPresent()) {
				return ResponseEntity.badRequest().body("Turma não Encontrado na base de Dados");
			}
			
			List<CourseClass> studentCourseClasses = student.get().getCourseClasses();

			if(studentCourseClasses.contains(courseClass.get())) {
				
				savedPresenc = service.savePresenc(savedPresenc);
				return new ResponseEntity<Serializable>(savedPresenc, HttpStatus.CREATED);	
			} else {
				return ResponseEntity.badRequest().body("Você não está matriculado nessa turma");
			}
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	private Presenc convert(PresencDTO dto) {
		Presenc presenc = new Presenc();
		presenc.setCode(dto.getCode());
		presenc.setDate(dto.getDate());
		presenc.setHour(dto.getHour());
		
		CourseClass courseClass = courseClassService.findById(dto.getCourseClassId())
				.orElseThrow(() -> new BusinessRuleException("Não foi encontrado uma Matéria com esse código"));
		
		presenc.setCourseClass(courseClass);
		
		Student student = studentService.findById(dto.getStudentId())
				.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para a matricula informada"));
		
		presenc.setStudent(student);
		
		return presenc;
	}
}
