package com.warehouse.returning.infrastructure.adapter.primary.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPackageRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;

@Component("returnRequestApiValidator")
public class ReturnRequestApiValidator extends RequestValidator<ReturnRequestApi> {

    @Override
    public Result<Void, List<String>> validateBody(final ReturnRequestApi request) {
        final List<String> errors = new ArrayList<>();
        if (request.requests().isEmpty()) {
            errors.add("Request must contain at least one package");
        }

        validateRequests(request.requests(), errors);

        return !errors.isEmpty() ? Result.failure(errors) : Result.success();
    }

    private void validateRequests(final List<ReturnPackageRequestApi> requests, final List<String> errors) {
        requests.forEach(request -> {
            if (request.shipmentId() == null || request.shipmentId().value() == null) {
                errors.add("Shipment id must be provided");
            }
            if (StringUtils.isEmpty(request.reason())) {
                errors.add("Reason must be provided");
            }
            if (request.departmentCode() == null || StringUtils.isEmpty(request.departmentCode().value())) {
                errors.add("Department code must be provided");
            }
            if (request.userId() == null || request.userId().value() == null) {
                errors.add("User id must be provided");
            }
            if (request.reasonCode() == null || StringUtils.isEmpty(request.reasonCode().value())) {
                errors.add("Reason code must be provided");
            } else {
                try {
                    com.warehouse.returning.domain.enumeration.ReasonCode.valueOf(request.reasonCode().value());
                } catch (final IllegalArgumentException e) {
                    errors.add("Invalid reason code: " + request.reasonCode().value());
                }
            }
        });
    }

    @Override
    public String getResourceName() {
        return ReturnRequestApi.class.getSimpleName();
    }
}
