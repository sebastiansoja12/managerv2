package com.warehouse.returning.infrastructure.adapter.primary.validator;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReturnStatusApiRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("changeReturnStatusApiValidator")
public class ChangeReturnStatusApiValidator extends RequestValidator<ChangeReturnStatusApiRequest> {

    @Override
    public Result<Void, List<String>> validateBody(final ChangeReturnStatusApiRequest changeReturnStatusApiRequest) {
        final List<String> errors = new ArrayList<>();

        if (changeReturnStatusApiRequest.returnPackageId() == null || changeReturnStatusApiRequest.returnPackageId().value() == null) {
            errors.add("Return package id must be provided");
        }

        if (changeReturnStatusApiRequest.returnStatus() == null) {
            errors.add("Return status must be provided");
        } else {
            try {
                ReturnStatus.valueOf(changeReturnStatusApiRequest.returnStatus());
            } catch (final IllegalArgumentException e) {
                errors.add("Invalid return status: " + changeReturnStatusApiRequest.returnStatus());
            }
        }
        return !errors.isEmpty() ? Result.failure(errors) : Result.success();
    }

    @Override
    public String getResourceName() {
        return ChangeReturnStatusApiRequest.class.getSimpleName();
    }
}
