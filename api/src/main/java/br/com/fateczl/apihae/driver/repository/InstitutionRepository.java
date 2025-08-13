package br.com.fateczl.apihae.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fateczl.apihae.domain.entity.Institution;

public interface InstitutionRepository extends JpaRepository<Institution, String> {
    
}
