package com.warehouse.returning.infrastructure.adapter.primary.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;

@Component(value = "changeReasonCodeRequestApiValidator")
public class ChangeReasonCodeRequestApiValidator extends RequestValidator<ChangeReasonCodeRequestApi> {

    @Override
    public Result<Void, List<String>> validateBody(final ChangeReasonCodeRequestApi request) {
        final List<String> errors = new ArrayList<>();

        if (request.returnPackageId() == null || request.returnPackageId().value() == null) {
            errors.add("Return package id must be provided");
        }

        if (request.reasonCode() == null) {
            errors.add("Reason code must be provided");
        } else {
            try {
                com.warehouse.returning.domain.enumeration.ReasonCode.valueOf(request.reasonCode());
            } catch (final IllegalArgumentException e) {
                errors.add("Invalid reason code: " + request.reasonCode());
            }
        }

        return !errors.isEmpty() ? Result.failure(errors) : Result.success();
    }

    @Override
    public String getResourceName() {
        return ChangeReasonCodeRequestApi.class.getSimpleName();
    }
}
