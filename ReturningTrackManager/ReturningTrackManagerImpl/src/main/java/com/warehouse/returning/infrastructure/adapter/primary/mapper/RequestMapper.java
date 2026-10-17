package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ChangeReturnStatusRequest;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.*;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReturnStatusApiRequest;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPackageRequestApi;

import java.util.List;

public abstract class RequestMapper {

    private RequestMapper() {}

    public static ChangeReasonCodeRequest map(final ChangeReasonCodeRequestApi apiRequest) {
        return new ChangeReasonCodeRequest(
                new ReturnPackageId(apiRequest.returnPackageId().value()),
                ReasonCode.valueOf(apiRequest.reasonCode())
        );
    }

    public static ReturnRequest map(final ReturnRequestApi returnApiRequest, final DecodedApiOperator decodedApiOperator) {
        final DepartmentId departmentId = decodedApiOperator.departmentId();
        final UserId userId = decodedApiOperator.userId();
        final List<ReturnPackageRequest> returnPackageRequests = returnApiRequest.requests()
                .stream()
                .map(RequestMapper::map)
                .toList();
        return new ReturnRequest(departmentId, userId, decodedApiOperator.operatorId(), returnPackageRequests);
    }

    private static ReturnPackageRequest map(final ReturnPackageRequestApi request) {
        return new ReturnPackageRequest(request.departmentId(), request.reason(),
                new ShipmentId(request.shipmentId().value()), new UserId(request.userId().value()),
                ReasonCode.valueOf(request.reasonCode().value()));
    }

    public static ChangeReturnStatusRequest map(final ChangeReturnStatusApiRequest request) {
        return new ChangeReturnStatusRequest(new ShipmentId(request.shipmentId().value()),
                ReturnStatus.valueOf(request.returnStatus()));
    }
}
