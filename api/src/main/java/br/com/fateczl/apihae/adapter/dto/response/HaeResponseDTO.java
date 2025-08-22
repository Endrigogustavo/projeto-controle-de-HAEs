package br.com.fateczl.apihae.adapter.dto.response;

import java.time.LocalDate;
import java.util.List;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.DimensaoHae;
import br.com.fateczl.apihae.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HaeResponseDTO {

    private String id;
    private String projectTitle;
    private String course;
    private String projectDescription;
    private Status status;
    private String professorName;
    private Boolean viewed;
    private DimensaoHae dimensao;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> comprovanteDoc;

    public HaeResponseDTO(Hae hae) {
        this.id = hae.getId();
        this.projectTitle = hae.getProjectTitle();
        this.course = hae.getCourse();
        this.projectDescription = hae.getProjectDescription();
        this.status = hae.getStatus();
        this.professorName = hae.getEmployee().getName();
        this.viewed = hae.getViewed();
        this.dimensao = hae.getDimensao();
        this.startDate = hae.getStartDate();
        this.endDate = hae.getEndDate();
        this.comprovanteDoc = hae.getComprovanteDoc();
    }
}