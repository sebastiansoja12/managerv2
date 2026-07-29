package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnRequestApi;

import java.util.List;

public class ReturnPackageOutboundMapper {

    public ReturnRequestApi map(final CreateReturnRequest request) {
        final ReturnRequestApi.ReturnPackageRequestApi packageRequest =
                new ReturnRequestApi.ReturnPackageRequestApi(
                        request.shipmentId(),
                        request.reason(),
                        request.departmentId(),
                        request.assignedTo(),
                        new ReturnRequestApi.ReasonCodeApi(request.reasonCode().name())
                );
        return new ReturnRequestApi(List.of(packageRequest));
    }
}
