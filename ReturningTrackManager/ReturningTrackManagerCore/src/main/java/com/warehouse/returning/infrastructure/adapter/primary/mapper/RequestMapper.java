package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.*;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;

import java.util.List;

public abstract class RequestMapper {

    private RequestMapper() {}

    public static ChangeReasonCodeRequest map(final ChangeReasonCodeRequestApi apiRequest) {
        return new ChangeReasonCodeRequest(
                new ReturnPackageId(apiRequest.returnPackageId().value()),
                ReasonCode.valueOf(apiRequest.reasonCode())
        );
    }

    public static ReturnRequest map(final ReturnRequestApi returnApiRequest, final DecodedApiTenant decodedApiTenant) {
        final DepartmentCode departmentCode = decodedApiTenant.departmentCode();
        final UserId userId = decodedApiTenant.userId();
        final List<ReturnPackageRequest> returnPackageRequests = returnApiRequest.requests()
                .stream()
                .map(ReturnPackageRequest::from)
                .toList();
        return new ReturnRequest(departmentCode, userId, returnPackageRequests);
    }
}

