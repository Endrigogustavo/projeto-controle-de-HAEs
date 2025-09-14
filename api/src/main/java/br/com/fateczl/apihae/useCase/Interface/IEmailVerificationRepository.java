package br.com.fateczl.apihae.useCase.Interface;

import java.util.Optional;

import br.com.fateczl.apihae.domain.entity.EmailVerification;

public interface IEmailVerificationRepository {
    Optional<EmailVerification> findByEmail(String email);

    Optional<EmailVerification> findByEmailAndCode(String email, String code);

    Optional<EmailVerification> findByCode(String code);

    void deleteByEmail(String email);

    EmailVerification save(EmailVerification emailVerification);

    void delete(EmailVerification emailVerification);

    void flush();
}
