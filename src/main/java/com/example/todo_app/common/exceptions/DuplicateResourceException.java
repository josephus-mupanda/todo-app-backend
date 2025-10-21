package com.example.todo_app.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateResourceException extends ResponseStatusException {
    public DuplicateResourceException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
