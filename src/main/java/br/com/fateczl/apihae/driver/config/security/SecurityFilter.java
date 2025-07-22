package br.com.fateczl.apihae.driver.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.useCase.util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JWTUtils JWTUtils;
    private final EmployeeRepository employeeRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoverToken(request);
        if (token != null) {
            try {
                String userId = JWTUtils.validateToken(token);
                if (userId != null) {
                    Employee employee = employeeRepository.findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                    var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    var authentication = new UsernamePasswordAuthenticationToken(employee, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String[] excludedPaths = {
            "/auth",
            "/docs/index.html",
            "/docs-client-service.html",
            "/v3/api-docs",
            "/swagger-ui-custom.html",
            "/swagger-ui.html",
            "/webjars",
            "/configuration",
            "/swagger-resources"
        };
        return Arrays.stream(excludedPaths).anyMatch(path::startsWith);
    }

        private String recoverToken(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) return null;

            return Arrays.stream(cookies)
                    .filter(cookie -> "auth_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

}
