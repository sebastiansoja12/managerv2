package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedDetail;
import lombok.extern.slf4j.Slf4j;

// TODO communication with external platform where client requests shipment delivery change etc.
@Slf4j
public class DeliveryInstructionServiceAdapter implements DeliveryInstructionServicePort {

    @Override
    public DeliveryMissedDetail getDeliveryInstruction(final ShipmentId shipmentId) {
        return null;
    }
}
