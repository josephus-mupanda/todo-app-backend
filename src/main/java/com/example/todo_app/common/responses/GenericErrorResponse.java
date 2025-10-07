package com.example.todo_app.common.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class GenericErrorResponse {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    @JsonProperty("path")
    private String path;

    public GenericErrorResponse(HttpStatus status, String message, String path) {
        this.statusCode = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    @JsonProperty("message")
    public String getActualMessage() {
        return message != null ? message.split("/")[0].trim() : null;
    }
}

