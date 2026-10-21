package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentDeliveryDto(DeliveryId deliveryId,
                                  ShipmentId shipmentId,
                                  DepartmentId originDepartmentId,
                                  DepartmentId destinationDepartmentId) {
}
