package br.com.fateczl.apihae.driver.config.cors;

import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpsRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        boolean isSecure = request.isSecure() || 
                           "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));

        if (!isSecure) {
            String redirectUrl = "https://" + request.getServerName() + request.getRequestURI();
            if (request.getQueryString() != null) {
                redirectUrl += "?" + request.getQueryString();
            }
            response.sendRedirect(redirectUrl);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nenhuma inicialização necessária
    }

    @Override
    public void destroy() {
        // Nenhuma destruição necessária
    }
}
