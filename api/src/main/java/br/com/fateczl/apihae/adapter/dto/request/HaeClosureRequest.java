package br.com.fateczl.apihae.adapter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HaeClosureRequest {

    // TCC fields
    private String tccRole;
    private Integer tccStudentCount;
    private String tccStudentNames;
    private String tccApprovedStudents;
    private String tccProjectInfo;
    
    // Estágio fields
    private String estagioStudentInfo;
    private String estagioApprovedStudents;
    
    // Apoio à Direção fields
    private String apoioType;
    private String apoioGeralDescription;
    private String apoioApprovedStudents;
    private String apoioCertificateStudents;
}
