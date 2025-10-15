package com.warehouse.department.domain.exception;

public class ForbiddenDepartmentTypeException extends RestException {
  public ForbiddenDepartmentTypeException(final String message) {
    super(message);
  }
}
