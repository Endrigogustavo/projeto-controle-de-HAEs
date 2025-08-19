package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.request.LoginRequest;
import br.com.fateczl.apihae.adapter.dto.request.ResetPasswordRequest;
import br.com.fateczl.apihae.adapter.dto.request.SendEmailCodeRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.useCase.service.AuthService;
import br.com.fateczl.apihae.useCase.util.CookieUtils;
import br.com.fateczl.apihae.useCase.util.JWTUtils;
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

    private final AuthService authService;
    private final CookieUtils cookieUtils;
    private final JWTUtils tokenService;

    @PostMapping("/send-email-code")
    public ResponseEntity<Object> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        authService.sendVerificationCode(
            request.getName(), 
            request.getEmail(),
            request.getCourse(), 
            request.getPassword(),
            request.getInstitution());

        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "E-mail de ativação enviado com sucesso."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Employee> verifyEmailCode(@RequestParam("token") String token, @RequestParam("institutionId") String institutionId, HttpServletResponse response) {
        Employee verifiedEmployee = authService.verifyEmailCode(token, institutionId);

        String jwtToken = tokenService.generateToken(verifiedEmployee);
        setTokenCookie(response, jwtToken);

        return ResponseEntity.ok(verifiedEmployee);
    }

    @PostMapping("/login")
    public ResponseEntity<Employee> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Employee authenticatedEmployee = authService.login(request.getEmail(), request.getPassword());

        String token = tokenService.generateToken(authenticatedEmployee);
        setTokenCookie(response, token);

        return ResponseEntity.ok(authenticatedEmployee);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        authService.sendPasswordResetToken(email);
        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "Se o e-mail estiver cadastrado, um link para redefinição de senha foi enviado."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Senha redefinida com sucesso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        cookieUtils.DeleteCookies(response);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Logout realizado com sucesso."));
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        cookieUtils.CreateCookies(response, token);
    }
}