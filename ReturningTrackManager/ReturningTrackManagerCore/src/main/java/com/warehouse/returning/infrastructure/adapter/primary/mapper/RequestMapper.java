package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.vo.ChangeReasonCodeRequest;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;

public abstract class RequestMapper {

    private RequestMapper() {}

    public static ChangeReasonCodeRequest map(final ChangeReasonCodeRequestApi apiRequest) {
        return new ChangeReasonCodeRequest(
                new ReturnPackageId(apiRequest.returnPackageId().value()),
                ReasonCode.valueOf(apiRequest.reasonCode())
        );
    }
}

