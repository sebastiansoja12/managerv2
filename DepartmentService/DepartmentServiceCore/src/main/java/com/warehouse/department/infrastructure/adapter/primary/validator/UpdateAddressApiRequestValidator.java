package com.warehouse.department.infrastructure.adapter.primary.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.department.domain.helper.Result;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateAddressApiRequest;

@Component(value = "updateAddressApiRequestValidator")
public class UpdateAddressApiRequestValidator extends DepartmentRequestValidator<UpdateAddressApiRequest> {

    @Override
    public Result<Void, List<String>> validateBody(final UpdateAddressApiRequest updateAddressApiRequest) {
        final List<String> errors = new ArrayList<>();
        if (updateAddressApiRequest.address() == null) {
            errors.add("Address cannot be empty");
        } else if (updateAddressApiRequest.address().street() == null || updateAddressApiRequest.address().street().isBlank()) {
            errors.add("Street cannot be empty");
        } else if (updateAddressApiRequest.address().city() == null || updateAddressApiRequest.address().city().isBlank()) {
            errors.add("City cannot be empty");
        } else if (updateAddressApiRequest.address().postalCode() == null || updateAddressApiRequest.address().postalCode().isBlank()) {
            errors.add("Postal code cannot be empty");
        } else if (updateAddressApiRequest.address().countryCode() == null || updateAddressApiRequest.address().countryCode().isBlank()) {
            errors.add("Country code cannot be empty");
        }
        if (updateAddressApiRequest.departmentCode() == null || updateAddressApiRequest.departmentCode().value().isBlank()) {
            errors.add("Department code cannot be empty");
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }

    @Override
    public String getResourceName() {
        return UpdateAddressApiRequest.class.getSimpleName();
    }
}
