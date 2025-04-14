package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import java.util.Map;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInstruction;

import lombok.extern.slf4j.Slf4j;

// TODO communication with external platform where client requests shipment delivery change etc.
@Slf4j
public class DeliveryInstructionServiceAdapter implements DeliveryInstructionServicePort {

    @Override
    public Map<ShipmentId, DeliveryMissedInstruction> getDeliveryInstruction(final DeliveryId deliveryId) {
        return Map.of();
    }
}
