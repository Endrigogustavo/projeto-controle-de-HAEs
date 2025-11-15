package br.com.fateczl.apihae.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hae_closure_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HaeClosureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "hae_id", nullable = false)
    @JsonBackReference
    private Hae hae;

    @Column(name = "coordenador_id", nullable = false)
    private String coordenadorId;

    @Column(name = "coordenador_name", nullable = true)
    private String coordenadorName;

    @Column(name = "approved_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime approvedAt;

    // Campos de informações de fechamento (snapshot no momento da aprovação)
    @Column(name = "tcc_role", nullable = true)
    private String tccRole;

    @Column(name = "tcc_student_count", nullable = true)
    private Integer tccStudentCount;

    @Column(name = "tcc_student_names", columnDefinition = "TEXT", nullable = true)
    private String tccStudentNames;

    @Column(name = "tcc_approved_students", columnDefinition = "TEXT", nullable = true)
    private String tccApprovedStudents;

    @Column(name = "tcc_project_info", columnDefinition = "TEXT", nullable = true)
    private String tccProjectInfo;

    @Column(name = "estagio_student_info", columnDefinition = "TEXT", nullable = true)
    private String estagioStudentInfo;

    @Column(name = "estagio_approved_students", columnDefinition = "TEXT", nullable = true)
    private String estagioApprovedStudents;

    @Column(name = "apoio_type", nullable = true)
    private String apoioType;

    @Column(name = "apoio_geral_description", columnDefinition = "TEXT", nullable = true)
    private String apoioGeralDescription;

    @Column(name = "apoio_approved_students", columnDefinition = "TEXT", nullable = true)
    private String apoioApprovedStudents;

    @Column(name = "apoio_certificate_students", columnDefinition = "TEXT", nullable = true)
    private String apoioCertificateStudents;
}

