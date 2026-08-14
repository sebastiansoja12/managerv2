package com.warehouse.department.domain.validator;

import com.warehouse.department.domain.enumeration.DepartmentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Validator {
    public void validateType(final DepartmentType departmentType) {
        if (departmentType == null) {
            throw new IllegalArgumentException("Department type cannot be null");
        }
    }
}
