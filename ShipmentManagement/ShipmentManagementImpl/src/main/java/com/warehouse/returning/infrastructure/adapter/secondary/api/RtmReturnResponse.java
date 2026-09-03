package com.warehouse.returning.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.UUID;

public record RtmReturnResponse(RtmCreateResponse.ReturnIdApi returnPackageId,
                                RtmCreateResponse.ShipmentIdApi shipmentId,
                                String returnStatus,
                                UUID pickupId,
                                ShipmentId returnShipmentId,
                                DepartmentId scanDepartmentId,
                                long returnVersion,
                                DepartmentId assignedDepartmentId,
                                DepartmentId returnedDepartmentId) {
}
