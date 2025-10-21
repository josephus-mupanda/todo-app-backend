package com.example.todo_app.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoValidFilesException extends ResponseStatusException {
    public NoValidFilesException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}