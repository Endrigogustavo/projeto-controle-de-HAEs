package br.com.fateczl.apihae.adapter.dto.response;

import java.time.LocalDateTime;

import br.com.fateczl.apihae.domain.entity.HaeClosureRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HaeClosureRecordDTO {

    private String id;
    private String coordenadorId;
    private String coordenadorName;
    private LocalDateTime approvedAt;
    
    // Campos de informações de fechamento
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

    public HaeClosureRecordDTO(HaeClosureRecord record) {
        this.id = record.getId();
        this.coordenadorId = record.getCoordenadorId();
        this.coordenadorName = record.getCoordenadorName();
        this.approvedAt = record.getApprovedAt();
        
        // Copiar informações de fechamento
        this.tccRole = record.getTccRole();
        this.tccStudentCount = record.getTccStudentCount();
        this.tccStudentNames = record.getTccStudentNames();
        this.tccApprovedStudents = record.getTccApprovedStudents();
        this.tccProjectInfo = record.getTccProjectInfo();
        this.estagioStudentInfo = record.getEstagioStudentInfo();
        this.estagioApprovedStudents = record.getEstagioApprovedStudents();
        this.apoioType = record.getApoioType();
        this.apoioGeralDescription = record.getApoioGeralDescription();
        this.apoioApprovedStudents = record.getApoioApprovedStudents();
        this.apoioCertificateStudents = record.getApoioCertificateStudents();
    }
}

