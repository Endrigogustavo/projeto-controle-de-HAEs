package br.com.fateczl.apihae.useCase.service.Hae;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IHaeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatusHae {
    private final IHaeRepository haeRepository;
    private final IEmployeeRepository employeeRepository;

    @Transactional
    public void wasViewed(String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + haeId));

        hae.setViewed(true);
        haeRepository.save(hae);
    }

    @Transactional
    public Hae toggleViewedStatus(String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + haeId));

        hae.setViewed(!hae.getViewed());
        return haeRepository.save(hae);
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

        employeeRepository.findById(coordenadorId)
                .filter(emp -> emp.getRole() == Role.COORDENADOR || emp.getRole() == Role.DEV)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coordenador com id " + coordenadorId + " não encontrado ou não é coordenador/dev."));

        hae.setStatus(newStatus);
        hae.setCoordenatorId(coordenadorId);
        return haeRepository.save(hae);
    }

}
