package br.com.fateczl.apihae.driver.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fateczl.apihae.domain.entity.EmailVerification;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmail(String email);

    Optional<EmailVerification> findByEmailAndCode(String email, String code);
}
