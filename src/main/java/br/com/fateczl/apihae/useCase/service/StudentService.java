package br.com.fateczl.apihae.useCase.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fateczl.apihae.adapter.dto.StudentRequest;
import br.com.fateczl.apihae.adapter.dto.StudentUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Student;
import br.com.fateczl.apihae.driver.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findStudentByRa(String ra) {
        return studentRepository.findById(ra)
                .orElseThrow(() -> new IllegalArgumentException("Estudante não encontrado com o RA: " + ra));
    }

    @Transactional
    public Student createStudent(StudentRequest student) {
        if (studentRepository.existsById(student.getRa())) {
            throw new IllegalArgumentException("Estudante já cadastrado com o RA: " + student.getRa());
        }

        Student newStudent = new Student();
        newStudent.setRa(student.getRa());
        newStudent.setName(student.getName());
        newStudent.setCourse(student.getCourse());
        newStudent.setEmail(student.getEmail());
        newStudent.setCourse(student.getCourse());
        newStudent.setPeriod(student.getPeriod());

        return studentRepository.save(newStudent);
    }

    @Transactional
    public Student updateStudent(String ra, StudentUpdateRequest student) {
        Student existingStudent = findStudentByRa(ra);

        existingStudent.setRa(ra);
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setCourse(student.getCourse());
        existingStudent.setPeriod(student.getPeriod());

        return studentRepository.save(existingStudent);
    }

    @Transactional
    public void deleteStudent(String ra) {
        if (!studentRepository.existsById(ra)) {
            throw new IllegalArgumentException("Estudante não encontrado com o RA: " + ra);
        }
        studentRepository.deleteById(ra);
    }

    @Transactional(readOnly = true)
    public List<Student> findStudentsByCourse(String course) {
        return studentRepository.findByCourseContainingIgnoreCase(course);
    }
}
