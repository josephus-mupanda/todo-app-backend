package com.example.todo_app.common.exceptions;
import com.example.todo_app.common.responses.GenericErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logger.error("Not Found Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<GenericErrorResponse> handleConflictException(ConflictException ex, WebRequest request) {
        logger.error("Conflict Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.CONFLICT,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        logger.error("Bad Request Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        logger.error("Unauthorized Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<GenericErrorResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        logger.error("Forbidden Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<GenericErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException ex, WebRequest request) {
        logger.error("Method Not Allowed Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<GenericErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request) {
        logger.error("Internal Server Error : {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Internal Server Error: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GenericErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        logger.error("Duplicate Resource Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.CONFLICT,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GenericErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        logger.error("Validation Exception: {}", ex.getMessage());
        GenericErrorResponse errorResponse = new GenericErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getReason(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
