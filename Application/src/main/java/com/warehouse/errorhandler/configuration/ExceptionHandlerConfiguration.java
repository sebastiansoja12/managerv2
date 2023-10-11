package com.warehouse.errorhandler.configuration;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.warehouse.errorhandler.domain.model.ErrorResponse;
import com.warehouse.exception.RestException;

@ControllerAdvice
public class ExceptionHandlerConfiguration {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {

        final ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(ex.getCode());
        return ResponseEntity.badRequest().body(errors);

    }
}
