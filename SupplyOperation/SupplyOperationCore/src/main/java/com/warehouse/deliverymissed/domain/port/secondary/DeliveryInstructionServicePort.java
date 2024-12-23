package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedDetails;

public interface DeliveryInstructionServicePort {
    DeliveryMissedDetails getDeliveryInstruction(final ShipmentId shipmentId);
}
