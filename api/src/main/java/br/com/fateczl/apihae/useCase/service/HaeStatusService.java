package br.com.fateczl.apihae.useCase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HaeStatusService {
    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;

    public void wasViewed(String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));
        hae.setViewed(true);
        haeRepository.save(hae);
    }

    public List<Hae> getHaeWasViewed() {
        return haeRepository.findByViewed(true);
    }

    public List<Hae> getHaeWasNotViewed() {
        return haeRepository.findByViewed(false);
    }

        @Transactional
    public Hae changeHaeStatus(String id, Status newStatus, String coordenadorId) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        Employee coordenador = employeeRepository.findById(coordenadorId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Coordenador com id " + coordenadorId + " não encontrado."));

        if (coordenador.getRole() != Role.COORDENADOR) {
            throw new IllegalArgumentException("Empregado com ID " + coordenadorId
                    + " não é um coordenador. Apenas coordenadores podem mudar o status da HAE.");
        }

        hae.setStatus(newStatus);
        hae.setCoordenatorId(coordenadorId);
        return haeRepository.save(hae);
    }

}
