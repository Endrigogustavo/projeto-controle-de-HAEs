package br.com.fateczl.apihae.adapter.dto.response;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Modality;
import br.com.fateczl.apihae.domain.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class HaeDetailDTO {

    private String id;
    private String projectTitle;
    private String professorName;
    private Status status;
    private String course;
    private HaeType projectType;
    private Modality modality;
    private Integer weeklyHours;
    private List<String> dayOfWeek;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, String> weeklySchedule;
    private String projectDescription;
    private String observations;
    private List<String> students;
    private Boolean viewed;
    private String coordenatorName;

    public HaeDetailDTO(Hae hae, String coordenatorName) {
        this.id = hae.getId();
        this.projectTitle = hae.getProjectTitle();
        this.professorName = hae.getEmployee().getName();
        this.status = hae.getStatus();
        this.course = hae.getCourse();
        this.projectType = hae.getProjectType();
        this.modality = hae.getModality();
        this.weeklyHours = hae.getWeeklyHours();
        this.dayOfWeek = hae.getDayOfWeek();
        this.startDate = hae.getStartDate();
        this.endDate = hae.getEndDate();
        this.weeklySchedule = hae.getWeeklySchedule();
        this.projectDescription = hae.getProjectDescription();
        this.observations = hae.getObservations();
        this.students = hae.getStudents();
        this.viewed = hae.getViewed();
        this.coordenatorName = coordenatorName;
    }
}