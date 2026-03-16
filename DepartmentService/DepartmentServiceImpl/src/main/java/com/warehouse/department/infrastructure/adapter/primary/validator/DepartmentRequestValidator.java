package com.warehouse.department.infrastructure.adapter.primary.validator;

import com.warehouse.department.domain.helper.Result;

import java.util.List;

public abstract class DepartmentRequestValidator<T> {
    public abstract Result<Void, List<String>> validateBody(final T t);
    public abstract String getResourceName();
}
