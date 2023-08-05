package com.warehouse.errorhandler.configuration;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.warehouse.errorhandler.domain.model.ErrorResponse;
import com.warehouse.exception.RestException;

@ControllerAdvice
public class ExceptionHandlerConfiguration {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleException(RestException ex) {

        final ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(ex.getCode());
        return new ResponseEntity<>(errors, HttpStatusCode.valueOf(ex.getCode()));

    }
}
