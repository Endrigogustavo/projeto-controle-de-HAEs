package br.com.fateczl.apihae.useCase.utils;

import org.springframework.context.annotation.Configuration;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CookieUtils {

    public void CreateCookies( HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
    }

    public void DeleteCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}