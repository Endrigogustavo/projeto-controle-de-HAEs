package br.com.fateczl.apihae.adapter.repository;

import br.com.fateczl.apihae.domain.entity.PasswordResetToken;
import br.com.fateczl.apihae.useCase.Interface.IPasswordResetTokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends IPasswordResetTokenRepository, JpaRepository<PasswordResetToken, Long> {
}