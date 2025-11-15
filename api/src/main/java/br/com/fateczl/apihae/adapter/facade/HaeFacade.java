package br.com.fateczl.apihae.adapter.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.request.HaeClosureRequest;
import br.com.fateczl.apihae.adapter.dto.response.HaeClosureRecordDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeHoursResponseDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.useCase.service.Hae.ManageHae;
import br.com.fateczl.apihae.useCase.service.Hae.ShowHae;
import br.com.fateczl.apihae.useCase.service.Hae.StatusHae;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HaeFacade {
    private final ShowHae showHae;
    private final ManageHae manageHae;
    private final StatusHae statusHae;

    public Hae createHae(HaeRequest hae) {
        return manageHae.createHae(hae);
    }

    public HaeDetailDTO getHaeById(String id) {
        return showHae.getHaeById(id);
    }

    public void deleteHae(String id) {
        manageHae.deleteHae(id);
    }

    public List<Hae> getHaesByEmployeeId(String id) {
        return showHae.getHaesByEmployeeId(id);
    }

    public List<Hae> getHaesByEmployeeIdWithLimit(String employeeId) {
        return showHae.getHaesByEmployeeIdWithLimit(employeeId);
    }

    public List<HaeResponseDTO> getAllHaes() {
        return showHae.getAllHaes();
    }

    public Hae updateHaeStatus(String id, Status status, String coodenadorId) {
        return statusHae.changeHaeStatus(id, status, coodenadorId);
    }

    public List<HaeResponseDTO> getHaesByProfessorId(String professorId) {
        return showHae.getHaesByProfessorId(professorId);
    }

    public List<HaeResponseDTO> getHaesByCourse(String course) {
        return showHae.getHaesByCourse(course);
    }

    public List<Hae> getHaesByType(HaeType haeType) {
        return showHae.getHaesByType(haeType);
    }

    public Hae updateHae(String id, HaeRequest haeRequest) {
        return manageHae.updateHae(id, haeRequest);
    }

    public Hae createHaeAsCoordinator(String coordinatorId, String employeeId, HaeRequest haeRequest) {
        return manageHae.createHaeAsCoordinator(coordinatorId, employeeId, haeRequest);
    }

    public Hae toggleViewedStatus(String id) {
        try {
            return statusHae.toggleViewedStatus(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao alternar o status de visualização da HAE com ID: " + id, e);
        }
    }

    public List<Hae> getViewed() {
        return statusHae.getHaeWasViewed();
    }

    public List<Hae> getNotViewed() {
        return statusHae.getHaeWasNotViewed();
    }

    public List<HaeResponseDTO> getHaesByInstitutionId(String institutionId) {
        return showHae.getHaesByInstitutionId(institutionId);
    }

    public List<HaeResponseDTO> getHaeByStatus(Status status) {
        return showHae.getHaeByStatus(status);
    }

    public List<HaeResponseDTO> advancedHaeSearch(String institutionId, String course, HaeType type, Status status,
            Boolean viewed) {
        return showHae.searchHaes(institutionId, course, type, status, viewed);
    }

    public int getAllWeeklyHours() {
        return showHae.getAllWeeklyHours();
    }

    public HaeHoursResponseDTO getWeeklyHoursByHae(String haeId) {
        return showHae.getWeeklyHoursByHae(haeId);
    }

    public Hae requestClosure(String haeId, HaeClosureRequest request) {
        return manageHae.requestClosure(haeId, request);
    }

    public List<HaeClosureRecordDTO> getClosureRecordsByHaeId(String haeId) {
        return showHae.getClosureRecordsByHaeId(haeId);
    }
}
