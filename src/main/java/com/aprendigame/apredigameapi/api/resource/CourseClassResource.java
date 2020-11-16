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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprendigame.apredigameapi.api.dto.CourseClassDTO;
import com.aprendigame.apredigameapi.exception.BusinessRuleException;
import com.aprendigame.apredigameapi.model.entity.CourseClass;
import com.aprendigame.apredigameapi.model.entity.CoursesUnit;
import com.aprendigame.apredigameapi.model.entity.Student;
import com.aprendigame.apredigameapi.model.entity.Teacher;
import com.aprendigame.apredigameapi.service.CourseClassService;
import com.aprendigame.apredigameapi.service.CoursesUnitService;
import com.aprendigame.apredigameapi.service.StudentService;
import com.aprendigame.apredigameapi.service.TeacherService;

@RestController
@RequestMapping("/api/courseClass")
public class CourseClassResource {

	private CourseClassService service;

	private CoursesUnitService courseUnitService;

	private TeacherService teacherService;

	private StudentService studentService;
	
	private StudentResource studentResource;

	public CourseClassResource(CourseClassService service, CoursesUnitService courseUnitService,
			TeacherService teacherService, StudentService studentService, StudentResource studentResource) {
		this.service = service;
		this.courseUnitService = courseUnitService;
		this.teacherService = teacherService;
		this.studentService = studentService;
		this.studentResource = studentResource;
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity findCourseClass(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "courseUnit", required = false)String courseUnit,
			@RequestParam(value = "teacher", required = false)Long teacher) {
		
		CourseClass courseClassFilter = new CourseClass();
		courseClassFilter.setName(name);
		
		if(courseUnit != null) {
			Optional<CoursesUnit> courseUnitCourseClass = courseUnitService.findByCode(courseUnit);
			if(courseUnitCourseClass.isPresent()) {
				courseClassFilter.setCourseUnit(courseUnitCourseClass.get());
			} else {
				return ResponseEntity.badRequest().body("Curso não encontrado para o Código informado!");
			}
		}
		
		if(teacher != null) {
			Optional<Teacher> teacherCourseClass = teacherService.findById(teacher);
			if(teacherCourseClass.isPresent()) {
				courseClassFilter.setTeacher(teacherCourseClass.get());
			} else {
				return ResponseEntity.badRequest().body("Professor não encontrado, esse id não se incontra na nossa base de dados");
			}
		}
		
		List<CourseClass> courseClasses = service.search(courseClassFilter);
		
		return ResponseEntity.ok(courseClasses);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/getCourseClass/{id}")
	public ResponseEntity getCourseClass(@PathVariable("id") Long id) {
		try {
			Optional<CourseClass> courseClass = service.findById(id);
			return new ResponseEntity(courseClass.get(), HttpStatus.OK);	
		} catch (BusinessRuleException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.findById(id).map(entity -> {
			
			if (entity.getStudents() != null) {
				for(Student student : entity.getStudents()) {
					studentResource.removeStudentFromCourseClass(student.getId(), entity);
				}
				service.deleteCourseClass(entity);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			service.deleteCourseClass(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
			new ResponseEntity("Classe não encontrada no banco de dados", HttpStatus.BAD_REQUEST));
	}

	@PostMapping("/save")
	public ResponseEntity<Serializable> save(@RequestBody CourseClassDTO dto) {
		try {
			CourseClass savedCourseClass = convert(dto);
			savedCourseClass = service.saveCourseClass(savedCourseClass);
			return new ResponseEntity<Serializable>(savedCourseClass, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id")Long id, @RequestBody CourseClassDTO dto ) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = convert(dto);
				courseClass.setId(entity.getId());
				courseClass.setStudents(entity.getStudents());
				courseClass.setQuizzes(entity.getQuizzes());
				courseClass.setTeacher(entity.getTeacher());
				
				if(!dto.getCourseUnitCode().equals(entity.getCourseUnit().getCode())) {
					CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
							.orElseThrow(() -> new BusinessRuleException("Não foi encontrado nenhum curso com esse código"));
					service.validateCodeAndCourseUnit(entity.getCode(), courseUnit);
					
					if(!dto.getCode().equals(entity.getCode())) {
						service.validateCodeAndCourseUnit(dto.getCode(), courseUnit);
					} else {
						courseClass.setCode(entity.getCode());
					}
				} else {
					courseClass.setCourseUnit(entity.getCourseUnit());
				}
				
				if(!dto.getCode().equals(entity.getCode())) {
						service.validateCodeAndCourseUnit(dto.getCode(), entity.getCourseUnit());
				}
				
				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/include_student")
	public ResponseEntity includeStudentOnCourseClass(@PathVariable("id") Long id, @RequestBody String studentRegistration) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Student studentFind = studentService.findByRegistration(studentRegistration)
						.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para essa matricula"));

				List<Student> students = courseClass.getStudents();
				
				//Testa se estudante informado já está na turma
				if(students.contains(studentFind)) {
					return ResponseEntity.badRequest().body("Estudante já cadastrado na Turma");
				}
				
				students.add(studentFind);
				courseClass.setStudents(students);
				
				//inclui turma na lista de turmas do estudante
				ResponseEntity response = studentResource.includeStudentInCourseClass(studentFind.getId(), courseClass);
				
				//testa se foi possivel incluir turma no estudante
				if (response.equals(ResponseEntity.ok(studentFind))) {
					service.updateCourseClass(courseClass);
					return ResponseEntity.ok(courseClass);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
				
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/remove_student")
	public ResponseEntity removeStudentOnCourseClass(@PathVariable("id") Long id, @RequestBody Long studentId) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				Student studentFind = studentService.findById(studentId)
						.orElseThrow(() -> new BusinessRuleException("Estudante não encontrado para essa matricula"));

				List<Student> students = courseClass.getStudents();
				
				//testa se estudante está na lista da turma
				if (!students.contains(studentFind)) {
					return ResponseEntity.badRequest().body("Estudante não está cadastrado nessa Turma");
				}
				
				
				students.remove(studentFind);
				courseClass.setStudents(students);
				
				ResponseEntity response = studentResource.removeStudentFromCourseClass(studentFind.getId(), courseClass);
				if(response.equals(ResponseEntity.ok(studentFind))) {
					service.updateCourseClass(courseClass);
					return ResponseEntity.ok(courseClass);
				} else {
					return ResponseEntity.badRequest().body(response.getBody());
				}
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/remove_teacher")
	public ResponseEntity removeTeacherOnCourseClass(@PathVariable("id") Long id, @RequestBody Long teacherId) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				
				Teacher teacher = teacherService.findById(teacherId)
						.orElseThrow(() -> new BusinessRuleException("Professor não encontrado para a matricula informada"));

				if(courseClass.getTeacher().equals(teacher)) {
					courseClass.setTeacher(null);
				} else {
					throw new BusinessRuleException("O professor informado não é o professor da Turma");
				}

				service.updateCourseClass(courseClass);
				
				
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}/include_teacher")
	public ResponseEntity includeTeacherOnCourseClass(@PathVariable("id") Long id, @RequestBody String teacherRegistration) {
		return service.findById(id).map(entity -> {
			try {
				CourseClass courseClass = entity;
				
				Teacher teacher = teacherService.findByRegistration(teacherRegistration)
						.orElseThrow(() -> new BusinessRuleException("Professor não encontrado para a matricula informada"));
				
				if (teacher.getCourseClasses().contains(courseClass)) {
					return ResponseEntity.badRequest().body("Professor já cadastrado na Turma");
				}

				courseClass.setTeacher(teacher);

				service.updateCourseClass(courseClass);
				return ResponseEntity.ok(courseClass);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Matéria não encontrado na nossa base de dados", HttpStatus.BAD_REQUEST));
	}

	private CourseClass convert(CourseClassDTO dto) {
		CourseClass courseClass = new CourseClass();
		courseClass.setName(dto.getName());
		courseClass.setCode(dto.getCode());
		courseClass.setQuizzes(dto.getQuizzes());

		CoursesUnit courseUnit = courseUnitService.findByCode(dto.getCourseUnitCode())
				.orElseThrow(() -> new BusinessRuleException("Não foi encontrado nenhum curso com esse código"));

		courseClass.setCourseUnit(courseUnit);

		if(dto.getTeacherId() != null) {
			Optional<Teacher> teacher = teacherService.findById(dto.getTeacherId());
			if (teacher.isPresent()) {
				courseClass.setTeacher(teacher.get());
			}
		}
		return courseClass;
	}
}
