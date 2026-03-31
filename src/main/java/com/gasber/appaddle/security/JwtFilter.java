package com.gasber.appaddle.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
        String path = request.getRequestURI();

        // rutas públicas
// rutas públicas
if (path.contains("/admin/login") ||
    path.contains("/admin/crear") ||
    path.contains("/swagger-ui") ||
    path.contains("/v3/api-docs") ||
    path.contains("/canchas")) {

    filterChain.doFilter(request, response);
    return;
}

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
       
        if (!jwtUtil.esValido(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}