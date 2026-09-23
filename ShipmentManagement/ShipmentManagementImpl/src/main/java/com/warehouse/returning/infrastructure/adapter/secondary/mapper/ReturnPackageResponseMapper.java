package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;
import com.warehouse.returning.infrastructure.adapter.secondary.api.RtmCreateResponse;
import com.warehouse.returning.infrastructure.adapter.secondary.api.RtmReturnResponse;

import java.util.List;

public class ReturnPackageResponseMapper {

    public List<CreatedReturn> map(final RtmCreateResponse response) {
        return response.processReturn().stream()
                .map(this::map)
                .toList();
    }

    public ReturnState map(final RtmReturnResponse response) {
        return new ReturnState(
                new ReturnPackageId(response.returnPackageId().value()),
                new ShipmentId(response.shipmentId().value()),
                ReturnStatus.valueOf(response.returnStatus()),
                response.pickupId(),
                response.returnShipmentId(),
                response.scanDepartmentId(),
                response.returnVersion(),
                response.assignedDepartmentId(),
                response.returnedDepartmentId()
        );
    }

    private CreatedReturn map(final RtmCreateResponse.CreatedReturnApi response) {
        return new CreatedReturn(
                new ShipmentId(response.shipmentId().value()),
                new ReturnPackageId(response.returnId().value()),
                ReturnStatus.valueOf(response.processStatus())
        );
    }
}
