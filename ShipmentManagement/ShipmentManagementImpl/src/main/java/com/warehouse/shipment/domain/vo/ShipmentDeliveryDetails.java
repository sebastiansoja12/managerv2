package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDateTime;

public record ShipmentDeliveryDetails(DepartmentId originDepartmentId,
                                      DepartmentId destinationDepartmentId,
                                      LocalDateTime plannedDeliveryAt,
                                      LocalDateTime deliveredAt) {

    public static final ShipmentDeliveryDetails NONE = new ShipmentDeliveryDetails(null, null, null, null);

    public ShipmentDeliveryDetails keepingOriginOf(final ShipmentDeliveryDetails current) {
        if (current.originDepartmentId() == null) {
            return this;
        }
        return new ShipmentDeliveryDetails(current.originDepartmentId(), destinationDepartmentId, plannedDeliveryAt,
                deliveredAt);
    }
}
