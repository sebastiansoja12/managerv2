package com.warehouse.departmentmap.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

import java.util.Map;

public class DepartmentMapException extends ProblemDetailsException {

    private final String code;

    public DepartmentMapException(final int status, final String code, final String message) {
        super("about:blank", "Department map request rejected", status, message, null, Map.of("code", code));
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DepartmentMapException invalid(final String message) {
        return new DepartmentMapException(400, "DEPARTMENT_MAP_INVALID", message);
    }

    public static DepartmentMapException notFound(final String code, final String message) {
        return new DepartmentMapException(404, code, message);
    }

    public static DepartmentMapException conflict(final String code, final String message) {
        return new DepartmentMapException(409, code, message);
    }
}
