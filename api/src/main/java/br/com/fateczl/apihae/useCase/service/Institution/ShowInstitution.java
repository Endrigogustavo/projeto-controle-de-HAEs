package br.com.fateczl.apihae.useCase.service.Institution;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.adapter.dto.response.InstitutionResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
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
    public Institution getInstitutionByInstitutionCode(Integer institutionCode) {
        return institutionRepository.findByInstitutionCode(institutionCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Instituição não encontrada com código: " + institutionCode));
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

}
