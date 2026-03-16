package com.warehouse.department.infrastructure.adapter.primary.validator;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.ChangeDepartmentEmailApiRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "changeDepartmentEmailApiRequestValidator")
public class ChangeDepartmentEmailApiRequestValidator extends DepartmentRequestValidator<ChangeDepartmentEmailApiRequest>{
    @Override
    public Result<Void, List<String>> validateBody(final ChangeDepartmentEmailApiRequest request) {
        final List<String> errors = new ArrayList<>();
        if (request.departmentCode() == null || request.departmentCode().value().isBlank()) {
            errors.add("Department code cannot be empty");
        }
        if (request.email() == null || request.email().isBlank()) {
            errors.add("Email cannot be empty");
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }

    @Override
    public String getResourceName() {
        return ChangeDepartmentEmailApiRequest.class.getSimpleName();
    }
}
