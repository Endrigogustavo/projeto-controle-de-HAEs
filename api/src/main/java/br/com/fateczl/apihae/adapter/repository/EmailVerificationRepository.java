package br.com.fateczl.apihae.adapter.repository;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.useCase.Interface.IEmailVerificationRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailVerificationRepository extends IEmailVerificationRepository, JpaRepository<EmailVerification, String>  {
}