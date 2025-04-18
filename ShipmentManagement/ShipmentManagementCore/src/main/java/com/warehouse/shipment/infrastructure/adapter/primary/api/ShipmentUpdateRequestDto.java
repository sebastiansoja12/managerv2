package com.warehouse.shipment.infrastructure.adapter.primary.api;

public final class ShipmentUpdateRequestDto {
	private final ShipmentIdDto shipmentId;
	private final PersonDto sender;
	private final PersonDto recipient;
	private final String destination;
    private final String token;
	private final ShipmentUpdateTypeDto shipmentUpdateType;

	public ShipmentUpdateRequestDto(final ShipmentIdDto shipmentId,
                                    final PersonDto sender,
                                    final PersonDto recipient,
                                    final String destination,
                                    final String token,
									final ShipmentUpdateTypeDto shipmentUpdateType) {
		this.shipmentId = shipmentId;
		this.sender = sender;
		this.recipient = recipient;
        this.destination = destination;
        this.token = token;
        this.shipmentUpdateType = shipmentUpdateType;
    }

	public ShipmentUpdateTypeDto getShipmentUpdateType() {
		return shipmentUpdateType;
	}

	public String getDestination() {
		return destination;
	}

	public ShipmentIdDto getShipmentId() {
		return shipmentId;
	}

	public PersonDto getSender() {
		return sender;
	}

	public PersonDto getRecipient() {
		return recipient;
	}

    public String getToken() {
        return token;
    }
}
