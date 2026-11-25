package com.warehouse.shipment.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentDeliveryReadModelData(ShipmentId shipmentId,
                                            DepartmentId originDepartmentId,
                                            DepartmentId destinationDepartmentId,
                                            DepartmentId currentDepartmentId,
                                            LocalDateTime plannedDeliveryAt,
                                            LocalDateTime deliveredAt,
                                            SupplierCode courierCode,
                                            String courierName) {
}
