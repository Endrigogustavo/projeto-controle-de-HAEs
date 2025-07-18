package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.LoginRequest;
import br.com.fateczl.apihae.adapter.dto.SendEmailCodeRequest;
import br.com.fateczl.apihae.adapter.dto.VerifyEmailCodeRequest;
import br.com.fateczl.apihae.useCase.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints para autenticação e autorização")
public class AuthController {

    private final AuthService authService;
   
    @PostMapping("/send-email-code")
    public ResponseEntity<Object> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        String verificationCode = authService.sendVerificationCode(request.getName(), request.getEmail(), request.getCourse(), request.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("mensagem",
                "E-mail de confirmação enviado com sucesso. (Código: " + verificationCode + ")"));
    }

    @PostMapping("/verify-email-code")
    public ResponseEntity<Object> verifyEmailCode(@Valid @RequestBody VerifyEmailCodeRequest request, HttpServletResponse response) {
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
        Cookie cookie = new Cookie("auth_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); 
        response.addCookie(cookie);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Logout bem-sucedido. Por favor, descarte seu token de autenticação."));
    }

    @GetMapping("/check-cookie")
    public ResponseEntity<Object> checkCookie(@CookieValue(value = "auth_token", required = false) String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("mensagem", "Cookie 'auth_token' não encontrado."));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {
            String username = authentication.getName();
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Usuário autenticado: " + username));
        }

        return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("mensagem", "Não autenticado."));
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }


}
