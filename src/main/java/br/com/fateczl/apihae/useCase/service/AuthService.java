package br.com.fateczl.apihae.useCase.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.useCase.util.JWTUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;



    @Transactional(readOnly = true)
    public String login(String email, String plainPassword) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        if (!passwordEncoder.matches(plainPassword, employee.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        String token = jwtUtils.generateToken(employee);
        if (token == null || token.isBlank()) {
            return "Error";
        }

        return token;
    }

    @Transactional
    public String sendVerificationCode(String name, String email, String plainPassword) {
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já registrado.");
        }

        String hashedPassword = passwordEncoder.encode(plainPassword);
        return emailVerificationService.generateAndSaveVerificationCode(email, name, hashedPassword);
    }

    @Transactional
    public String verifyEmailCode(String email, String code) {
        EmailVerification verification = emailVerificationService.findValidVerification(email, code)
                .orElseThrow(() -> new IllegalArgumentException("Código inválido ou expirado."));

        if (employeeRepository.findByEmail(email).isPresent()) {
            emailVerificationService.deleteVerification(verification);
            throw new IllegalArgumentException("Email já associado com uma conta registrada.");
        }

        Employee newEmployee = new Employee();
        newEmployee.setName(verification.getName());
        newEmployee.setEmail(verification.getEmail());
        newEmployee.setPassword(verification.getPassword());
        newEmployee.setRole(Role.PROFESSOR);

        Employee savedEmployee = employeeRepository.save(newEmployee);
        emailVerificationService.deleteVerification(verification);

        String token = jwtUtils.generateToken(savedEmployee);
        if (token == null || token.isBlank()) {
            return "Error";
        }

        return token;
    }

}
