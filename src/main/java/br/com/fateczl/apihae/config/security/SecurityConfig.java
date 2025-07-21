package br.com.fateczl.apihae.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.fateczl.apihae.adapter.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] PUBLIC_MATCHERS = {
        "/auth/**",
        "/docs/index.html",
        "/docs-client-service.html",
        "/docs-client-service/**",
        "/v3/api-docs/**",
        "/swagger-ui-custom.html",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/webjars/**",
        "/configuration/**",
        "/swagger-resources/**",
        "/employee/get-my-user"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
