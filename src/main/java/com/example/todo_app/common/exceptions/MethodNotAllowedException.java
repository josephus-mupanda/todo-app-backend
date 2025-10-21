package com.example.todo_app.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MethodNotAllowedException extends ResponseStatusException {
    public MethodNotAllowedException(String reason) {
        super(HttpStatus.METHOD_NOT_ALLOWED, reason);
    }
}
