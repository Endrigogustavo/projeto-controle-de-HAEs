package br.com.fateczl.apihae.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.request.StudentRequest;
import br.com.fateczl.apihae.adapter.dto.request.StudentUpdateRequest;
import br.com.fateczl.apihae.useCase.service.Student.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Student", description = "Endpoints para manipular os estudantes do sistema")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentService.findAllStudents());
    }

    @GetMapping("/getById/{ra}")
    public ResponseEntity<?> getStudentById(String ra) {
        return ResponseEntity.ok(studentService.findStudentByRa(ra));
    }

    @GetMapping("/getByCourse/{course}")
    public ResponseEntity<?> getStudentsByCourse(String course) {
        return ResponseEntity.ok(studentService.findStudentsByCourse(course));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
    }

    // Trocar o StudentRequest por um StudentUpdateRequest
    @PutMapping("/update/{ra}")
    public ResponseEntity<?> updateStudent(@PathVariable String ra, @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(ra, request));
    }

    @DeleteMapping("/delete/{ra}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String ra) {
        studentService.deleteStudent(ra);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enrollHae/{studentId}/{haeId}")
    public ResponseEntity<?> enrollStudentInHae(@PathVariable String studentId, @PathVariable String haeId) {
        return ResponseEntity.ok("Student with ID: " + studentId + " enrolled in HAE with ID: " + haeId);
    }

}
