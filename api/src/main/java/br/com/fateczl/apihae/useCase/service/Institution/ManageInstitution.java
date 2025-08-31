package br.com.fateczl.apihae.useCase.service.Institution;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.factory.InstitutionFactory;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import br.com.fateczl.apihae.useCase.service.Employee.ShowEmployee;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ManageInstitution {

    private final InstitutionRepository institutionRepository;
    private final ShowEmployee showEmployee;

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

    @Transactional()
    public void setHaeQtd(int quantidade, String userId, String institutionId) {
        Optional.of(quantidade).filter(q -> q >= 0)
                .orElseThrow(() -> new IllegalArgumentException("Quantidade não pode ser negativa."));

        EmployeeResponseDTO employee = showEmployee.getEmployeeById(userId);
        if (employee == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (employee.getRole() != Role.DIRETOR && employee.getRole() != Role.ADMIN && employee.getRole() != Role.DEV) {
            throw new IllegalArgumentException(
                    "Usuário não é um diretor ou administrador da unidade, impossivel definir a quantidade de HAEs.");
        }

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + institutionId));
        institution.setHaeQtd(quantidade);
        institutionRepository.save(institution);

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
