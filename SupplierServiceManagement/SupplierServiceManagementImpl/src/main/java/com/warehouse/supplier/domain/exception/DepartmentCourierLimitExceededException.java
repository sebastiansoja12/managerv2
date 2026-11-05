package com.warehouse.supplier.domain.exception;

public class DepartmentCourierLimitExceededException extends BusinessException {
    public DepartmentCourierLimitExceededException(final String message) {
        super(message);
    }
}
