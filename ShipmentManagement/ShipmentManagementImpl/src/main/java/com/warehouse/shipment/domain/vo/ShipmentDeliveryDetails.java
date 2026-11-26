package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

import java.time.LocalDateTime;

public record ShipmentDeliveryDetails(DepartmentId originDepartmentId,
                                      DepartmentId destinationDepartmentId,
                                      DepartmentId currentDepartmentId,
                                      LocalDateTime plannedDeliveryAt,
                                      LocalDateTime deliveredAt,
                                      SupplierCode courierCode,
                                      String courierName) {

    public static final ShipmentDeliveryDetails NONE = new ShipmentDeliveryDetails(null, null, null, null, null);

    public ShipmentDeliveryDetails(final DepartmentId originDepartmentId,
                                   final DepartmentId destinationDepartmentId,
                                   final DepartmentId currentDepartmentId,
                                   final LocalDateTime plannedDeliveryAt,
                                   final LocalDateTime deliveredAt) {
        this(originDepartmentId, destinationDepartmentId, currentDepartmentId, plannedDeliveryAt, deliveredAt,
                null, null);
    }
}
