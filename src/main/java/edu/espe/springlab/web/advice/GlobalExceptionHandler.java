package edu.espe.springlab.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 del negocio
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex, WebRequest request){
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 409 cuando existe correos duplicados
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex, WebRequest request){
        return error(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // 400 por validaciones del Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        Map<String, String> errors = new HashMap<>();
        for(FieldError field : ex.getBindingResult().getFieldErrors()){
            errors.put(field.getField(), field.getDefaultMessage());
        }
        body.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 500 genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex, WebRequest request){
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    // Construye el JSON estándar de error
    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(body);
    }
}
