package br.com.fateczl.apihae.adapter.dto.response;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Modality;
import br.com.fateczl.apihae.domain.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.fateczl.apihae.domain.enums.DimensaoHae;

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
    private DimensaoHae dimensao;
    private String coordenatorName;
    private LocalDateTime updatedAt;
    
    // Closure information fields
    private String tccRole;
    private Integer tccStudentCount;
    private String tccStudentNames;
    private String tccApprovedStudents;
    private String tccProjectInfo;
    private String estagioStudentInfo;
    private String estagioApprovedStudents;
    private String apoioType;
    private String apoioGeralDescription;
    private String apoioApprovedStudents;
    private String apoioCertificateStudents;

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
        this.dimensao = hae.getDimensao();
        this.coordenatorName = coordenatorName;
        this.updatedAt = hae.getUpdatedAt();
        
        // Populate closure fields
        this.tccRole = hae.getTccRole();
        this.tccStudentCount = hae.getTccStudentCount();
        this.tccStudentNames = hae.getTccStudentNames();
        this.tccApprovedStudents = hae.getTccApprovedStudents();
        this.tccProjectInfo = hae.getTccProjectInfo();
        this.estagioStudentInfo = hae.getEstagioStudentInfo();
        this.estagioApprovedStudents = hae.getEstagioApprovedStudents();
        this.apoioType = hae.getApoioType();
        this.apoioGeralDescription = hae.getApoioGeralDescription();
        this.apoioApprovedStudents = hae.getApoioApprovedStudents();
        this.apoioCertificateStudents = hae.getApoioCertificateStudents();
    }
}