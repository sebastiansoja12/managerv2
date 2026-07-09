package com.warehouse.department.infrastructure.adapter.primary.validator;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.ChangeDepartmentStatusApi;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "changeDepartmentStatusApiValidator")
public class ChangeDepartmentStatusApiValidator extends DepartmentRequestValidator<ChangeDepartmentStatusApi>{
    @Override
    public Result<Void, List<String>> validateBody(final ChangeDepartmentStatusApi request) {
        final List<String> errors = new ArrayList<>();
        if (request.departmentCode() == null || request.departmentCode().value().isBlank()) {
            errors.add("Department code cannot be empty");
        }
        try {
            Department.Status.valueOf(request.status());
        } catch (final IllegalArgumentException e) {
            errors.add("Invalid status: " + request.status());
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }

    @Override
    public String getResourceName() {
        return ChangeDepartmentStatusApi.class.getSimpleName();
    }
}
