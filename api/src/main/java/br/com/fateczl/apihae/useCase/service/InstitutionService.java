package br.com.fateczl.apihae.useCase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.adapter.dto.InstitutionCreateRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final EmployeeRepository employeeRepository;
    private final HaeRepository haeRepository;
    private final EmployeeService employeeService;

public void createInstitution(InstitutionCreateRequest request) {
    Institution institution = new Institution();
    institution.setName(request.getInstitutionName());
    institution.setInstitutionCode(request.getInstitutionCode());
    institution.setHaeQtd(request.getHaeQtd());
    institution.setAddress(request.getAddress());
    institution.setActive(true);

    institutionRepository.save(institution);
}


    public int getHaeQtd(String id) throws IllegalArgumentException{
        Institution inst = institutionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));
        return inst.getHaeQtd();
    }

    public void setHaeQtd(int quantidade, String userId, String institutionId) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }

        Employee employee = employeeService.getEmployeeById(userId);
        if (employee == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (employee.getRole() != Role.DIRETOR && employee.getRole() != Role.ADMIN){
            throw new IllegalArgumentException(
                    "Usuário não é um diretor ou administrador da unidade, impossivel definir a quantidade de HAEs.");
        }

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + institutionId));
        institution.setHaeQtd(quantidade);
        institutionRepository.save(institution);

    }

    public List<Institution> listAllInstitutions() {
        return institutionRepository.findAll();
    }

    public List<Employee> getEmployeesByInstitutionId(String institutionId) {
        return employeeRepository.findByInstitutionId(institutionId);
    }

    public List<Hae> getHaesByInstitutionId(String institutionId) {
        return haeRepository.findByInstitutionId(institutionId);
    }

    public Institution getInstitutionById(String institutionId) {
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com ID: " + institutionId));
    }

}
