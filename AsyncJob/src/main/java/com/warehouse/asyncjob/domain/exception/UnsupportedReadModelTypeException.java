package com.warehouse.asyncjob.domain.exception;

import com.warehouse.asyncjob.domain.enumeration.ReadModelType;

public class UnsupportedReadModelTypeException extends RuntimeException {

    public UnsupportedReadModelTypeException(final ReadModelType readModelType) {
        super("Read model rebuilder not found for type: " + readModelType);
    }
}
