package com.warehouse.exceptioncatcher.domain.port.primary;

import com.warehouse.exceptionhandler.exception.RestException;

public interface ExceptionCatcherPort {
    void handle(RestException ex);
}
