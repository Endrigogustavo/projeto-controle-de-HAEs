package br.com.fateczl.apihae.useCase.Interface;

import java.util.List;
import java.util.Optional;

import br.com.fateczl.apihae.domain.entity.HaeClosureRecord;

public interface IHaeClosureRecordRepository {
    HaeClosureRecord save(HaeClosureRecord record);

    Optional<HaeClosureRecord> findById(String id);

    List<HaeClosureRecord> findByHaeId(String haeId);

    List<HaeClosureRecord> findByCoordenadorId(String coordenadorId);

    List<HaeClosureRecord> findAll();
}

