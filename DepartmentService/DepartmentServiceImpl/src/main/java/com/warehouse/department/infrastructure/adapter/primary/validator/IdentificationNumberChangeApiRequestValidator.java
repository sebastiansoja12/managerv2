package com.warehouse.department.infrastructure.adapter.primary.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.IdentificationNumberChangeApiRequest;

@Component(value = "identificationNumberChangeApiRequestValidator")
public class IdentificationNumberChangeApiRequestValidator extends DepartmentRequestValidator<IdentificationNumberChangeApiRequest>{
    @Override
    public Result<Void, List<String>> validateBody(final IdentificationNumberChangeApiRequest identificationNumberChangeApiRequest) {
        final List<String> errors = new ArrayList<>();
        if (identificationNumberChangeApiRequest.identificationNumber() == null || identificationNumberChangeApiRequest.identificationNumber().isBlank()) {
            errors.add("Identification number cannot be empty");
        }
        if (identificationNumberChangeApiRequest.departmentCode() == null || identificationNumberChangeApiRequest.departmentCode().value().isBlank()) {
            errors.add("Department code cannot be empty");
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }

    @Override
    public String getResourceName() {
        return IdentificationNumberChangeApiRequest.class.getSimpleName();
    }
}
