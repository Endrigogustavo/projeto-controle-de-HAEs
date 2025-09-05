package br.com.fateczl.apihae.useCase.service.Institution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.InstitutionResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.factory.InstitutionFactory;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShowInstitution {

    private final InstitutionRepository institutionRepository;
    private final EmployeeRepository employeeRepository;
    private final HaeRepository haeRepository;

    public void createInstitution(InstitutionCreateRequest request) {
        institutionRepository.findByName(request.getName()).ifPresent(inst -> {
            throw new IllegalArgumentException("Nome de instituição já em uso.");
        });

        institutionRepository.findByInstitutionCode(request.getInstitutionCode()).ifPresent(inst -> {
            throw new IllegalArgumentException("Código de instituição já em uso.");
        });

        Institution institution = InstitutionFactory.create(request);

        institutionRepository.save(institution);
    }

    @Transactional(readOnly = true)
    public int getHaeQtdHours(String id) throws IllegalArgumentException {
        Institution inst = institutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));
        return inst.getHaeQtd();
    }

    @Transactional(readOnly = true)
    public List<InstitutionResponseDTO> listAllInstitutions() {
        List<Institution> institutions = institutionRepository.findAll();

        return institutions.stream()
                .map(InstitutionResponseDTO::new)
                .collect(Collectors.toList());
    }  

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByInstitutionId(String institutionId) {
        return employeeRepository.findByInstitutionId(institutionId);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByInstitutionId(String institutionId) {
        return haeRepository.findByInstitutionId(institutionId);
    }

    @Transactional(readOnly = true)
    public Institution getInstitutionById(String institutionId) {
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com ID: " + institutionId));
    }

    @Transactional()
    public Institution updateInstitution(Integer id, InstitutionUpdateRequest request) {
        Institution institution = institutionRepository.findByInstitutionCode(id)
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com código: " + id));

        institutionRepository.findByName(request.getName())
                .filter(existing -> !existing.getId().equals(institution.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "O nome '" + request.getName() + "' já está em uso por outra instituição.");
                });

        Optional<Institution> existingByCode = institutionRepository
                .findByInstitutionCode(request.getInstitutionCode())
                .filter(existing -> !existing.getId().equals(institution.getId()));

        existingByCode.ifPresent(existing -> {
            throw new IllegalArgumentException("O código '" + request.getInstitutionCode() + "' já está em uso.");
        });

        institution.setName(request.getName());
        institution.setAddress(request.getAddress());
        institution.setHaeQtd(request.getHaeQtd());
        institution.setInstitutionCode(request.getInstitutionCode());

        return institutionRepository.save(institution);
    }
}
