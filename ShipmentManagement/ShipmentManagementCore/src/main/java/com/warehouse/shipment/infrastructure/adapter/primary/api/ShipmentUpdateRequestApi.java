package com.warehouse.shipment.infrastructure.adapter.primary.api;

public final class ShipmentUpdateRequestApi {
	private final ShipmentIdDto shipmentId;
	private final PersonApi sender;
	private final PersonApi recipient;
	private final String destination;
    private final String token;
	private final ShipmentUpdateTypeApi shipmentUpdateType;

	public ShipmentUpdateRequestApi(final ShipmentIdDto shipmentId,
									final PersonApi sender,
									final PersonApi recipient,
									final String destination,
									final String token,
									final ShipmentUpdateTypeApi shipmentUpdateType) {
		this.shipmentId = shipmentId;
		this.sender = sender;
		this.recipient = recipient;
        this.destination = destination;
        this.token = token;
        this.shipmentUpdateType = shipmentUpdateType;
    }

	public ShipmentUpdateTypeApi getShipmentUpdateType() {
		return shipmentUpdateType;
	}

	public String getDestination() {
		return destination;
	}

	public ShipmentIdDto getShipmentId() {
		return shipmentId;
	}

	public PersonApi getSender() {
		return sender;
	}

	public PersonApi getRecipient() {
		return recipient;
	}

    public String getToken() {
        return token;
    }
}
