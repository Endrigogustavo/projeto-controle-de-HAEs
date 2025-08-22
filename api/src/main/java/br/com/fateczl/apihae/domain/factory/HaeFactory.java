package br.com.fateczl.apihae.domain.factory;

import java.util.List;
import java.util.Map;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Status;

public class HaeFactory {

    public static Hae createHae(HaeRequest request, Employee employee, Institution institution,
            Map<String, String> weeklyScheduleFlattened) {
        Hae hae;
        hae = new Hae();
        hae.setEmployee(employee);
        hae.setInstitution(institution);
        hae.setNameEmployee(employee.getName());
        hae.setProjectTitle(request.getProjectTitle());
        hae.setWeeklyHours(request.getWeeklyHours());
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setObservations(request.getObservations());
        hae.setStatus(Status.PENDENTE);
        hae.setCourse(request.getCourse());
        hae.setDimensao(request.getDimensao());
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setCoordenatorId("Sem coordenador definido");
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setTimeRange(request.getTimeRange());
        hae.setProjectDescription(request.getProjectDescription());
        hae.setWeeklySchedule(weeklyScheduleFlattened);

        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            hae.setStudents(request.getStudentRAs());
        } else {
            hae.setStudents(List.of());
        }

        return hae;
    }

    public static Hae createByCoordinator(Employee employee, HaeRequest request, Institution institution,
            Employee coordinator, Map<String, String> weeklyScheduleFlattened) {
        Hae hae = new Hae();
        hae.setEmployee(employee);
        hae.setInstitution(institution);
        hae.setNameEmployee(employee.getName());
        hae.setProjectTitle(request.getProjectTitle());
        hae.setWeeklyHours(request.getWeeklyHours());
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setObservations(request.getObservations());
        hae.setStatus(Status.PENDENTE);
        hae.setCourse(request.getCourse());
        hae.setDimensao(request.getDimensao()); 
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setCoordenatorId(coordinator.getId());
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setTimeRange(request.getTimeRange());
        hae.setProjectDescription(request.getProjectDescription());
        hae.setWeeklySchedule(weeklyScheduleFlattened);
        
        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            hae.setStudents(request.getStudentRAs());
        } else {
            hae.setStudents(List.of());
        }

        return hae;
    }

}
