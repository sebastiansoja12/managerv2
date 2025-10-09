package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record ShipmentReturnRequestApi(ShipmentIdDto shipmentId, String reason, DepartmentCode departmentCode,
                                       UserId issuedBy) {
}
