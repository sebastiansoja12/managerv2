package com.warehouse.department.infrastructure.adapter.primary.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;

@Component("departmentCreateApiRequestValidator")
public abstract class DepartmentCreateApiRequestValidator extends RequestValidator<DepartmentCreateApiRequest> {

    @Override
    public Result<Void, List<String>> validateBody(final DepartmentCreateApiRequest request) {
        return Result.success();
    }

    @Override
    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
