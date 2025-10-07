package com.example.todo_app.common.config;

import com.example.todo_app.common.responses.GenericErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.FORBIDDEN,
                "Forbidden: " + accessDeniedException.getMessage(),
                request.getRequestURI()
        );

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}

