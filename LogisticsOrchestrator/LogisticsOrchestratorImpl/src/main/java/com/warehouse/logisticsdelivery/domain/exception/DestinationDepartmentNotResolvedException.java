package com.warehouse.logisticsdelivery.domain.exception;

public class DestinationDepartmentNotResolvedException extends RuntimeException {

    public DestinationDepartmentNotResolvedException(final String message) {
        super(message);
    }
}
