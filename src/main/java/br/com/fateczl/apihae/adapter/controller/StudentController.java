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

import br.com.fateczl.apihae.adapter.dto.StudentRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
@SecurityRequirement(name = "cookieAuth") 
public class StudentController {
    
    //
    //Rotas para implementar
    //
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok("List of all students");
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getStudentById(String id) {
        return ResponseEntity.ok("Details of student with ID: " + id);
    }

    @GetMapping("/getByCourse/{course}")
    public ResponseEntity<?> getStudentsByCourse(String course) {
        return ResponseEntity.ok("List of students in course: " + course);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Student created successfully");
    }

    //Trocar o StudentRequest por um StudentUpdateRequest
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok("Student with ID: " + id + " updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        return ResponseEntity.ok("Student with ID: " + id + " deleted successfully");
    }

    @PostMapping("/enrollHae/{studentId}/{haeId}")
    public ResponseEntity<?> enrollStudentInHae(@PathVariable String studentId, @PathVariable String haeId) {
        return ResponseEntity.ok("Student with ID: " + studentId + " enrolled in HAE with ID: " + haeId);
    }

}
