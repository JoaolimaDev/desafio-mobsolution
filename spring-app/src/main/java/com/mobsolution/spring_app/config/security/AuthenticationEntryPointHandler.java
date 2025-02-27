package com.mobsolution.spring_app.config.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);
        String jsonResponse = "{\"status\": \"Não autorizado\", \"mensagem\": \"É necessária autenticação para acessar este recurso.\"}";
        response.getWriter().write(jsonResponse);

    }



}