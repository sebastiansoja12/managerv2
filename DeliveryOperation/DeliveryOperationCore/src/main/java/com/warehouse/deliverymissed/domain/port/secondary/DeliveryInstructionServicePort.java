package com.warehouse.deliverymissed.domain.port.secondary;

import java.util.Map;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInstruction;

public interface DeliveryInstructionServicePort {
    Map<ShipmentId, DeliveryMissedInstruction> getDeliveryInstruction(final DeliveryId deliveryId);
}
