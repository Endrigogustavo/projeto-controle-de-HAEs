package br.com.fateczl.apihae.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.useCase.Interface.IInstitutionRepository;

public interface InstitutionRepository extends IInstitutionRepository, JpaRepository<Institution, String> {
}
