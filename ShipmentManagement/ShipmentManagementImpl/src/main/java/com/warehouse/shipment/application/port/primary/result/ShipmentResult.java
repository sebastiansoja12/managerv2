package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public record ShipmentResult(ShipmentSnapshot snapshot,
                             DepartmentCode destination) {
}
