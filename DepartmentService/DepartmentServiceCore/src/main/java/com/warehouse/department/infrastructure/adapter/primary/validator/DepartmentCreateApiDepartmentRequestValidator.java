package com.warehouse.department.infrastructure.adapter.primary.validator;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "departmentCreateApiRequestValidator")
public abstract class DepartmentCreateApiDepartmentRequestValidator extends DepartmentRequestValidator<DepartmentCreateApiRequest> {

    @Override
    public Result<Void, List<String>> validateBody(final DepartmentCreateApiRequest request) {
        return Result.success();
    }

    @Override
    public String getResourceName() {
        return DepartmentCreateApiRequest.class.getSimpleName();
    }
}
