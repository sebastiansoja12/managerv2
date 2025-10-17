package com.warehouse.department.infrastructure.adapter.primary.validator;

import java.util.List;

import com.warehouse.department.domain.helper.Result;

public abstract class RequestValidator<T> {
    public abstract Result<Void, List<String>> validateBody(final T t);
    public abstract String getResourceName();
}
