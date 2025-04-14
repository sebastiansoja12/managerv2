package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.enumeration.DeliveryInstruction;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInstruction;

import lombok.extern.slf4j.Slf4j;

// TODO communication with external platform where client requests shipment delivery change etc.
@Slf4j
public class DeliveryInstructionServiceMockAdapter implements DeliveryInstructionServicePort {

	@Override
	public Map<ShipmentId, DeliveryMissedInstruction> getDeliveryInstruction(final DeliveryId deliveryId) {
		final Map<ShipmentId, DeliveryMissedInstruction> deliveryMissedInstructions = new HashMap<>();
		deliveryMissedInstructions.put(new ShipmentId(1L),
				new DeliveryMissedInstruction(DeliveryInstruction.NO_DELIVERY, "", new ShipmentId(1L),
				Instant.now().plusSeconds(3600)));
		return deliveryMissedInstructions;
	}
}
