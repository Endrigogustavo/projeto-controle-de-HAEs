package br.com.fateczl.apihae.driver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fateczl.apihae.domain.entity.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByCourseContainingIgnoreCase(String course);
}