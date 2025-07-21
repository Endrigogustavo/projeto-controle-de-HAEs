package br.com.fateczl.apihae.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Modality;
import br.com.fateczl.apihae.domain.enums.Status;

import java.time.LocalDate;

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

    @Column(name = "projectType", nullable = false)
    private String projectType;

    @Column(name = "dayOfWeek", nullable = false)
    private String dayOfWeek;

    @Column(name = "timeRange", nullable = false)
    private String timeRange;

    @Column(name = "resultAchieved", nullable = false, columnDefinition = "TEXT")
    private String resultAchieved;

    @ElementCollection
    @CollectionTable(name = "hae_Cronograma", joinColumns = @JoinColumn(name = "hae_id"))
    @Column(name = "item_cronograma", nullable = false, columnDefinition = "TEXT")
    private List<String> cronograma;

    @Column(name = "projectDescription", nullable = false, columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "observations", columnDefinition = "TEXT")
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

    @Column(name = "comprovanteDoc")
    private String comprovanteDoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "haeType", nullable = false)
    private HaeType haeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality", nullable = false)
    private Modality modality;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    @JsonManagedReference
    private Employee employee;

//    @ManyToMany
//    @JoinTable(
//        name = "hae_student",
//        joinColumns = @JoinColumn(name = "hae_id"),
//        inverseJoinColumns = @JoinColumn(name = "student_ra")
//    )
//    @JsonManagedReference
//    private List<Student> students;

    @Column(name = "students", nullable = false)
    private List<String> students;



    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
   
}