package br.com.fateczl.apihae.adapter.facade;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.useCase.service.Auth.ManageAuth;
import br.com.fateczl.apihae.useCase.service.Auth.SenderEmailAuth;
import br.com.fateczl.apihae.useCase.util.CookieUtils;
import br.com.fateczl.apihae.useCase.util.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthFacade {
    private final ManageAuth manageAuth;
    private final SenderEmailAuth senderEmailAuth;
    private final CookieUtils cookieUtils;
    private final JWTUtils tokenService;

    public void sendEmailCode(String name, String email, String course, String password, String institution) {
        senderEmailAuth.sendVerificationCode(name, email, course, password, institution);
    }

    public Employee verifyEmailCode(String token, Integer institutionCode, HttpServletResponse response) {
        var verifiedEmployee = manageAuth.verifyEmailCode(token, institutionCode);
        var jwtToken = tokenService.generateToken(verifiedEmployee);
        setTokenCookie(response, jwtToken);
        return verifiedEmployee;
    }

    public Employee login(String email, String password, HttpServletResponse response) {
        var authenticatedEmployee = manageAuth.login(email, password);
        var jwtToken = tokenService.generateToken(authenticatedEmployee);
        setTokenCookie(response, jwtToken);
        return authenticatedEmployee;
    }

    public void forgotPassword(Map<String, String> payload) {
        String email = payload.get("email");
        senderEmailAuth.sendPasswordResetToken(email);
    }

    public void resetPassword(String token, String newPassword) {
        manageAuth.resetPassword(token, newPassword);
    }

    public void logout(HttpServletResponse response) {
        cookieUtils.DeleteCookies(response);
    }

    public void setTokenCookie(HttpServletResponse response, String token) {
        cookieUtils.CreateCookies(response, token);
    }

}
