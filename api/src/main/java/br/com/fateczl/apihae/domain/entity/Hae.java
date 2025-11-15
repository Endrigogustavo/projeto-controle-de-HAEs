package br.com.fateczl.apihae.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.fateczl.apihae.domain.enums.DimensaoHae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Modality;
import br.com.fateczl.apihae.domain.enums.Status;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hae")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hae {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "nameEmployee", nullable = false)
    private String nameEmployee;

    @Column(name = "course", nullable = false)
    private String course;

    @Column(name = "projectTitle", nullable = false)
    private String projectTitle;

    @Column(name = "weeklyHours", nullable = false)
    private Integer weeklyHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "projectType", nullable = false)
    private HaeType projectType;

    @ElementCollection
    @CollectionTable(name = "hae_days_of_week", joinColumns = @JoinColumn(name = "hae_id"))
    @Column(name = "day_of_week")
    private List<String> dayOfWeek;

    @Column(name = "timeRange", nullable = false)
    private String timeRange;

    @Column(name = "projectDescription", nullable = false, columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "observations", columnDefinition = "TEXT", nullable = true)
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDENTE;

    @Column(name = "coordenatorId", nullable = true)
    private String coordenatorId;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "viewed", nullable = false)
    private Boolean viewed = false;

    @ElementCollection
    @CollectionTable(name = "hae_docs", joinColumns = @JoinColumn(name = "hae_id"))
    @Column(name = "comprovanteDoc")
    private List<String> comprovanteDoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "dimensao", nullable = false)
    private DimensaoHae dimensao;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality", nullable = false)
    private Modality modality;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    @JsonManagedReference
    private Employee employee;

    @ElementCollection
    @CollectionTable(name = "hae_students", joinColumns = @JoinColumn(name = "hae_id"))
    @Column(name = "student_ra", nullable = true)
    private List<String> students;

    @ElementCollection
    @CollectionTable(name = "hae_weekly_schedule", joinColumns = @JoinColumn(name = "hae_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "time_range")
    private Map<String, String> weeklySchedule;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    @JsonBackReference
    private Institution institution;

    // Closure information fields
    @Column(name = "tccRole", nullable = true)
    private String tccRole;

    @Column(name = "tccStudentCount", nullable = true)
    private Integer tccStudentCount;

    @Column(name = "tccStudentNames", columnDefinition = "TEXT", nullable = true)
    private String tccStudentNames;

    @Column(name = "tccApprovedStudents", columnDefinition = "TEXT", nullable = true)
    private String tccApprovedStudents;

    @Column(name = "tccProjectInfo", columnDefinition = "TEXT", nullable = true)
    private String tccProjectInfo;

    @Column(name = "estagioStudentInfo", columnDefinition = "TEXT", nullable = true)
    private String estagioStudentInfo;

    @Column(name = "estagioApprovedStudents", columnDefinition = "TEXT", nullable = true)
    private String estagioApprovedStudents;

    @Column(name = "apoioType", nullable = true)
    private String apoioType;

    @Column(name = "apoioGeralDescription", columnDefinition = "TEXT", nullable = true)
    private String apoioGeralDescription;

    @Column(name = "apoioApprovedStudents", columnDefinition = "TEXT", nullable = true)
    private String apoioApprovedStudents;

    @Column(name = "apoioCertificateStudents", columnDefinition = "TEXT", nullable = true)
    private String apoioCertificateStudents;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}