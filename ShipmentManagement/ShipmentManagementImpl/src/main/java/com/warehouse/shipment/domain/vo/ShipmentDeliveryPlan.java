package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;

public record ShipmentDeliveryPlan(DepartmentId originDepartmentId,
                                   DepartmentId destinationDepartmentId,
                                   PickupMethod pickupMethod,
                                   DeliveryMethod deliveryMethod,
                                   PickupPointId pickupPointId,
                                   PickupPointId deliveryPickupPointId) {
}
