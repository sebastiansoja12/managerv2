package com.warehouse.exceptioncatcher.infrastructure.adapter.secondary;

import com.warehouse.exceptioncatcher.domain.port.secondary.ExceptionRepository;
import com.warehouse.exceptionhandler.exception.RestException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionRepositoryImpl implements ExceptionRepository {

    @Override
    public void saveException(RestException ex) {

    }
}
