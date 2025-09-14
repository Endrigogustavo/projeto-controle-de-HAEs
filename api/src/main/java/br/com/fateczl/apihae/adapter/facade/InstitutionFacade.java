package br.com.fateczl.apihae.adapter.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.InstitutionResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.useCase.service.Hae.ShowHae;
import br.com.fateczl.apihae.useCase.service.Institution.ManageInstitution;
import br.com.fateczl.apihae.useCase.service.Institution.ShowInstitution;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InstitutionFacade {
    private final ManageInstitution manageInstitution;
    private final ShowInstitution showInstitution;
    private final ShowHae showHae;

    public void setAvailableHaesCount(int count, String userId, String institutionId) {
        manageInstitution.setHaeQtd(count, userId, institutionId);
    }

    public int getAvailableHaesCount(String institutionId) {
        return showInstitution.getHaeQtdHours(institutionId);
    }

    public int getRemainingHours(String institutionId) {
        int qtdHaeFatec = showInstitution.getHaeQtdHours(institutionId);
        int weeklyHours = showHae.getWeeklyHoursAllHaesInstitutionByCurrentSemester(institutionId);
        return qtdHaeFatec - weeklyHours;
    }

    public void createInstitution(InstitutionCreateRequest request) {
        manageInstitution.createInstitution(request);
    }

    public Institution updateInstitution(Integer id, InstitutionUpdateRequest request) {
        return manageInstitution.updateInstitution(id, request);
    }

    public Institution getInstitutionById(String institutionId) {
        return showInstitution.getInstitutionById(institutionId);
    }

    public Institution getInstitutionByInstitutionCode(Integer institutionCode) {
        return showInstitution.getInstitutionByInstitutionCode(institutionCode);
    }

    public List<InstitutionResponseDTO> getAllInstitutions() {
        return showInstitution.listAllInstitutions();
    }

    public List<Employee> getEmployeesByInstitutionId(String institutionId) {
        return showInstitution.getEmployeesByInstitutionId(institutionId);
    }

    public List<Hae> getHaesByInstitutionId(String institutionId) {
        return showInstitution.getHaesByInstitutionId(institutionId);
    }

}
