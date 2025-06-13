package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.HaeRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HaeService {

    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;

    public HaeService(HaeRepository haeRepository, EmployeeRepository employeeRepository) {
        this.haeRepository = haeRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Hae createHae(HaeRequest request) { 
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível criar HAE."));

        Hae newHae = new Hae();
        newHae.setEmployee(employee); 
        newHae.setProjectTitle(request.getProjectTitle());
        newHae.setWeeklyHours(request.getWeeklyHours());
        newHae.setStartDate(request.getStartDate());
        newHae.setEndDate(request.getEndDate());
        newHae.setObservations(request.getObservation());
        newHae.setStatus(Status.PENDENTE);

        return haeRepository.save(newHae);
    }

    @Transactional(readOnly = true)
    public Hae getHaeById(String id) {
        return haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));
    }

    @Transactional
    public void deleteHae(String id) {
        if (!haeRepository.existsById(id)) {
            throw new IllegalArgumentException("HAE não encontrado com ID: " + id);
        }
        haeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByEmployeeId(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Empregado com ID " + employeeId + " não encontrado.");
        }
        return haeRepository.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<Hae> getAllHaes() {
        return haeRepository.findAll();
    }

    @Transactional
    public Hae changeHaeStatus(String id, Status newStatus, String coordenadorId) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        Employee coordenador = employeeRepository.findById(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador com id " + coordenadorId + " não encontrado."));

        if (coordenador.getRole() != Role.COORDENADOR) {
            throw new IllegalArgumentException("Empregado com ID " + coordenadorId
                    + " não é um coordenador. Apenas coordenadores podem mudar o status da HAE.");
        }

        hae.setStatus(newStatus);
        hae.setCoordenatorId(coordenadorId);
        return haeRepository.save(hae);
    }
}
