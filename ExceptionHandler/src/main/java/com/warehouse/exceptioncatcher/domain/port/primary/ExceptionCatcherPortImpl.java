package com.warehouse.exceptioncatcher.domain.port.primary;


import com.warehouse.exceptioncatcher.domain.port.secondary.ExceptionRepository;
import com.warehouse.exceptionhandler.exception.RestException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionCatcherPortImpl implements ExceptionCatcherPort {

    private final ExceptionRepository exceptionRepository;

    @Override
    public void handle(RestException ex) {
        exceptionRepository.saveException(ex);
    }
}
