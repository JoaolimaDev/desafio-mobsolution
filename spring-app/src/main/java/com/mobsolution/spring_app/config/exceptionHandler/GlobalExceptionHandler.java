package com.mobsolution.spring_app.config.exceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mobsolution.spring_app.domain.dto.Response;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> HandleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
  
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> HandleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> HandleUsernameNotFoundException(UsernameNotFoundException ex){

        Response response = new Response(ex.getMessage(), HttpStatus.BAD_REQUEST.name());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        if (ex.getMessage().contains("tb_presenca_participante_id_data_key")) {
            

            Response response = new Response("Participante já possui presença cadastrada nesta data.", HttpStatus.CONFLICT.name());


            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

    
        return new ResponseEntity<>(new Response("Erro de integridade nos dados.",  HttpStatus.BAD_REQUEST.name()), HttpStatus.BAD_REQUEST);
    }

}