package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReasonCodeApi;

public record ShipmentReturnRequestApi(ShipmentIdDto shipmentId, String reason, ReasonCodeApi reasonCode,
                                       DepartmentCode departmentCode, String returnStatus) {
}
