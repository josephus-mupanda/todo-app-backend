package com.example.todo_app.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileUploadException extends ResponseStatusException {
    public FileUploadException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public FileUploadException(String reason, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, reason, cause);
    }


}