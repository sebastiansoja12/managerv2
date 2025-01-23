package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;

import lombok.Builder;


@Builder
public class Shipment {
    private ShipmentId shipmentId;
    private String senderEmail;
    private String recipientEmail;
    private String shipmentStatus;
    private Boolean locked;

    public Shipment(final ShipmentId shipmentId,
                    final String senderEmail,
                    final String recipientEmail,
                    final String shipmentStatus,
                    final Boolean locked) {
        this.shipmentId = shipmentId;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.shipmentStatus = shipmentStatus;
        this.locked = locked;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public Boolean isLocked() {
        return locked;
    }

    public static Shipment from(final ShipmentDto shipment) {
        final ShipmentId id = new ShipmentId(shipment.shipmentId().getValue());
		return new Shipment(id, shipment.sender().getEmail(),
				shipment.recipient().getEmail(), shipment.shipmentStatus().name(), shipment.locked());
	}
}
