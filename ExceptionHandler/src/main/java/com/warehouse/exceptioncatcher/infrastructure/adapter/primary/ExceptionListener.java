package com.warehouse.exceptioncatcher.infrastructure.adapter.primary;

import java.time.LocalDateTime;

import com.warehouse.exceptioncatcher.domain.port.primary.ExceptionCatcherPort;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.exceptionhandler.model.ErrorResponse;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
@AllArgsConstructor
public class ExceptionListener {

    private final ExceptionCatcherPort catcherPort;

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {
        catcherPort.handle(ex);
        final ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(ex.getCode());
        errors.setError(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatusCode.valueOf(errors.getStatus()));
    }
}