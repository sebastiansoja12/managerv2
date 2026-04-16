package com.warehouse.department.infrastructure.adapter.primary.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;

@Component(value = "departmentCreateApiRequestValidator")
public abstract class DepartmentCreateApiDepartmentRequestValidator extends DepartmentRequestValidator<DepartmentCreateApiRequest> {

    @Override
    public Result<Void, List<String>> validateBody(final DepartmentCreateApiRequest request) {
        final List<String> errors = new ArrayList<>();
        if (CollectionUtils.isEmpty(request.departments())) {
            errors.add("Departments list cannot be empty");
        }

        for (final DepartmentCreateApi department : request.departments()) {
            if (department.departmentCode() == null || department.departmentCode().value().isBlank()) {
                errors.add("Department code cannot be empty");
            }
            if (department.email() == null || department.email().isBlank()) {
                errors.add("Email cannot be empty");
            }
            if (department.telephoneNumber() == null || department.telephoneNumber().isBlank()) {
                errors.add("Telephone number cannot be empty");
            }
            if (department.postalCode() == null || department.postalCode().isBlank()) {
                errors.add("Postal code cannot be empty");
            }
            if (department.street() == null || department.street().isBlank()) {
                errors.add("Street cannot be empty");
            }
            if (department.city() == null || department.city().isBlank()) {
                errors.add("City cannot be empty");
            }
            if (department.departmentType() == null || department.departmentType().isBlank()) {
                errors.add("Department type cannot be empty");
            }
            if (department.nip() == null || department.nip().isBlank()) {
                errors.add("Nip cannot be empty");
            }
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }

    @Override
    public String getResourceName() {
        return DepartmentCreateApiRequest.class.getSimpleName();
    }
}
