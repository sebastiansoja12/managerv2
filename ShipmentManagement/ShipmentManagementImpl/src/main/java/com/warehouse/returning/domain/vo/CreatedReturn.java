package com.warehouse.returning.domain.vo;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record CreatedReturn(ShipmentId shipmentId, ReturnPackageId returnId, ReturnStatus status) {
}
