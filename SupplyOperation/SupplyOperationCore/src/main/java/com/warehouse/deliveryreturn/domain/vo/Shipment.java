package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Shipment {
    Long id;
    String senderEmail;
    String recipientEmail;
    String shipmentStatus;
    Boolean locked;
    
    
	public static Shipment from(final ShipmentDto shipment) {
		return new Shipment(shipment.shipmentId().getValue(), shipment.sender().getEmail(),
				shipment.recipient().getEmail(), shipment.shipmentStatus().name(), shipment.locked());
	}
}
