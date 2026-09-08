package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;

public record CreateDeliveryCommand(ShipmentId shipmentId,
                                    DepartmentId originDepartmentId,
                                    DepartmentId destinationDepartmentId,
                                    PickupMethod pickupMethod,
                                    DeliveryMethod method,
                                    PickupPointId pickupPointId,
                                    PickupPointId deliveryPickupPointId,
                                    SignatureId signatureId,
                                    Boolean signatureRequired,
                                    UserId userId) {
}
