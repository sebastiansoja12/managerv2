package com.warehouse.departmentmap.domain.exception;

public class DepartmentMapException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, NOT_FOUND, CONFLICT }

    private static final String INVALID_CODE = "DEPARTMENT_MAP_INVALID";

    private final Reason reason;
    private final String code;

    public DepartmentMapException(final Reason reason, final String code, final String message) {
        super(message);
        this.reason = reason;
        this.code = code;
    }

    public Reason reason() {
        return reason;
    }

    public String getCode() {
        return code;
    }

    public static DepartmentMapException invalid(final String message) {
        return new DepartmentMapException(Reason.INVALID_REQUEST, INVALID_CODE, message);
    }

    public static DepartmentMapException notFound(final String code, final String message) {
        return new DepartmentMapException(Reason.NOT_FOUND, code, message);
    }

    public static DepartmentMapException conflict(final String code, final String message) {
        return new DepartmentMapException(Reason.CONFLICT, code, message);
    }
}
