package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.enumeration.DeliveryInstruction;

import java.time.Instant;

public record DeliveryMissedInstruction(DeliveryInstruction deliveryInstruction,
                                        String instruction, ShipmentId shipmentId,
                                        Instant proposedDate) {
}
