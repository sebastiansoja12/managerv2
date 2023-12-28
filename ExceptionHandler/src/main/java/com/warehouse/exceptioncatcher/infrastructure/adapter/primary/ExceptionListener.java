package com.warehouse.exceptioncatcher.infrastructure.adapter.primary;

import com.warehouse.exceptioncatcher.domain.port.primary.ExceptionCatcherPort;
import com.warehouse.exceptionhandler.exception.RestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
@AllArgsConstructor
public class ExceptionListener {

    private final ExceptionCatcherPort catcherPort;

    @ExceptionHandler(RestException.class)
    void handleException(RestException ex) {
        catcherPort.handle(ex);
    }
}