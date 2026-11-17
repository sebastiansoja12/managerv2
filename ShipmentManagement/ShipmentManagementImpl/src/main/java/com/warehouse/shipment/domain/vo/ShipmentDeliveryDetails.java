package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDateTime;

public record ShipmentDeliveryDetails(DepartmentId originDepartmentId,
                                      DepartmentId destinationDepartmentId,
                                      DepartmentId currentDepartmentId,
                                      LocalDateTime plannedDeliveryAt,
                                      LocalDateTime deliveredAt) {

    public static final ShipmentDeliveryDetails NONE = new ShipmentDeliveryDetails(null, null, null, null, null);
}
