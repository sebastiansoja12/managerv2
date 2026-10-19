package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.PickupMethod;

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
