package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedDetail;

public interface DeliveryInstructionServicePort {
    DeliveryMissedDetail getDeliveryInstruction(final ShipmentId shipmentId);
}
