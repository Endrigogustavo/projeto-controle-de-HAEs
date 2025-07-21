package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.LoginRequest;
import br.com.fateczl.apihae.adapter.dto.SendEmailCodeRequest;
import br.com.fateczl.apihae.adapter.dto.VerifyEmailCodeRequest;
import br.com.fateczl.apihae.useCase.service.AuthService;
import br.com.fateczl.apihae.useCase.utils.CookieUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints para autenticação e autorização")
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    @PostMapping("/send-email-code")
    public ResponseEntity<Object> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        String verificationCode = authService.sendVerificationCode(request.getName(), request.getEmail(),
                request.getCourse(), request.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "E-mail de confirmação enviado com sucesso. (Código: " + verificationCode + ")"));
    }

    @PostMapping("/verify-email-code")
    public ResponseEntity<Object> verifyEmailCode(@Valid @RequestBody VerifyEmailCodeRequest request,
            HttpServletResponse response) {
        String token = authService.verifyEmailCode(request.getEmail(), request.getCode());
        setTokenCookie(response, token);
        return ResponseEntity.ok("Created");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request.getEmail(), request.getPassword());
        setTokenCookie(response, token);
        return ResponseEntity.ok("logado");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        cookieUtils.DeleteCookies(response);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Logout realizado com sucesso."));
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        cookieUtils.CreateCookies(response, token);
    }
}
