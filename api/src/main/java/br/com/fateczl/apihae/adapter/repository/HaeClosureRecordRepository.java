package br.com.fateczl.apihae.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fateczl.apihae.domain.entity.HaeClosureRecord;
import br.com.fateczl.apihae.useCase.Interface.IHaeClosureRecordRepository;

public interface HaeClosureRecordRepository extends IHaeClosureRecordRepository, JpaRepository<HaeClosureRecord, String> {
    
    @Query("SELECT r FROM HaeClosureRecord r WHERE r.hae.id = :haeId")
    List<HaeClosureRecord> findByHaeId(@Param("haeId") String haeId);

    @Query("SELECT r FROM HaeClosureRecord r WHERE r.coordenadorId = :coordenadorId")
    List<HaeClosureRecord> findByCoordenadorId(@Param("coordenadorId") String coordenadorId);
}

