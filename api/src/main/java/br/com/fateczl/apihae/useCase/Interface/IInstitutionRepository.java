package br.com.fateczl.apihae.useCase.Interface;

import java.util.List;
import java.util.Optional;

import br.com.fateczl.apihae.domain.entity.Institution;

public interface IInstitutionRepository {
    Optional<Institution> findByName(String name);

    Optional<Institution> findByInstitutionCode(Integer code);

    Institution save(Institution institution);

    Optional<Institution> findById(String id);

    void delete(Institution institution);

    void deleteById(String id);

    boolean existsById(String id);

    boolean existsByInstitutionCode(Integer code);

    List<Institution> findAll();
}
