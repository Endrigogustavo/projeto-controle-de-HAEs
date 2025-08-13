package br.com.fateczl.apihae.driver.repository;

import br.com.fateczl.apihae.domain.entity.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import br.com.fateczl.apihae.domain.entity.Employee;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByEmployee(Employee employee);
}