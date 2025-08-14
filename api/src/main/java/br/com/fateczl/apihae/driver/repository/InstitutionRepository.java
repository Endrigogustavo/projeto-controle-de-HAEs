package br.com.fateczl.apihae.driver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fateczl.apihae.domain.entity.Institution;

public interface InstitutionRepository extends JpaRepository<Institution, String> {
    Optional<Institution> findByName(String name);
    Optional<Institution> findByInstitutionCode(Integer code);
}
