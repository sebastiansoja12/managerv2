package com.warehouse.exceptionhandler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.exceptionhandler.model.ErrorResponse;

import java.time.LocalDateTime;


@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {
        final ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(ex.getCode());
        errors.setError(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatusCode.valueOf(errors.getStatus()));
    }
}
