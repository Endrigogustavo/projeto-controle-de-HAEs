package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.request.LoginRequest;
import br.com.fateczl.apihae.adapter.dto.request.ResetPasswordRequest;
import br.com.fateczl.apihae.adapter.dto.request.SendEmailCodeRequest;
import br.com.fateczl.apihae.adapter.facade.AuthFacade;
import br.com.fateczl.apihae.domain.entity.Employee;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints para autenticação, registro e autorização")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping("/send-email-code")
    public ResponseEntity<Object> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        authFacade.sendEmailCode(
                request.getName(),
                request.getEmail(),
                request.getCourse(),
                request.getPassword(),
                request.getInstitution());
        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "E-mail de ativação enviado com sucesso."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Employee> verifyEmailCode(@RequestParam("token") String token,
            @RequestParam("institutionCode") Integer institutionCode, HttpServletResponse response) {
        Employee verifiedEmployee = authFacade.verifyEmailCode(token, institutionCode, response);
        return ResponseEntity.ok(verifiedEmployee);
    }

    @PostMapping("/login")
    public ResponseEntity<Employee> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Employee authenticatedEmployee = authFacade.login(request.getEmail(), request.getPassword(), response);
        return ResponseEntity.ok(authenticatedEmployee);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, String> payload) {
        authFacade.forgotPassword(payload);
        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "Se o e-mail estiver cadastrado, um link para redefinição de senha foi enviado."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authFacade.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Senha redefinida com sucesso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        authFacade.logout(response);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Logout realizado com sucesso."));
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        authFacade.setTokenCookie(response, token);
    }
}