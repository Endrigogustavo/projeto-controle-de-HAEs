package br.com.fateczl.apihae.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, Employee employee) {
        this.token = token;
        this.employee = employee;
        this.expiryDate = LocalDateTime.now().plusHours(1);
    }
}