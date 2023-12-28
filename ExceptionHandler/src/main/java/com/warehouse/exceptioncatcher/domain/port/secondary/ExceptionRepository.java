package com.warehouse.exceptioncatcher.domain.port.secondary;

import com.warehouse.exceptionhandler.exception.RestException;

public interface ExceptionRepository {
    void saveException(RestException ex);
}
