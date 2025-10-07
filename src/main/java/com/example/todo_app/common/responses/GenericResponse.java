package com.example.todo_app.common.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
public class GenericResponse<T> {

    @JsonProperty("message")
    private String message;
    private T payload;

    public GenericResponse(String message, T payload) {
        this.message = message;
        this.payload = payload;
    }

    public static <T> ResponseEntity<GenericResponse<T>> createResponse(
            int status,
            String message,
            T payload
    ) {
        GenericResponse<T> response = new GenericResponse<>(message, payload);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<GenericResponse<T>> ok(String message, T payload) {
        return createResponse(200, message, payload);
    }

    public static <T> ResponseEntity<GenericResponse<T>> ok(String message) {
        return createResponse(200, message, null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> created(String message, T payload) {
        return createResponse(201, message, payload);
    }

    @JsonProperty("message")
    public String getActualMessage() {
        if (message != null) {
            String[] parts = message.split("/");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        return message;
    }
}
