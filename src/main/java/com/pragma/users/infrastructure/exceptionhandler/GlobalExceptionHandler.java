package com.pragma.users.infrastructure.exceptionhandler;

import com.pragma.users.application.dto.response.ErrorResponse;
import com.pragma.users.application.dto.response.ValidationErrorResponse;
import com.pragma.users.domain.exception.RestaurantNotFoundException;
import com.pragma.users.domain.exception.RoleNotFoundException;
import com.pragma.users.domain.exception.UserAlreadyExistsException;
import com.pragma.users.domain.exception.UserNotFoundException;
import com.pragma.users.domain.validation.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Usuario ya existe", ex.getMessage(), request);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(
            RoleNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Rol no encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRestaurantNotFound(
            RestaurantNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Restaurante no encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, "Datos inválidos", message, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validación de dominio",
                null,
                request.getRequestURI(),
                ex.getErrors().stream()
                        .map(ValidationErrorResponse::from)
                        .toList()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                "Forbidden",
                "Acceso no autorizado",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno", ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String error, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        error,
                        message,
                        request.getRequestURI(),
                        null
                )
        );
    }
}
