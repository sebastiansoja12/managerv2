package com.warehouse.shipment.infrastructure.api.dto;

public class ShipmentParcelDto {

    private final PersonDto sender;

    private final PersonDto recipient;

    private final ShipmentSizeDto shipmentSize;

    private final String destination;

    private final ShipmentStatusDto shipmentStatus;
    
    private final ParcelIdDto parcelRelatedId;

    private final double price;

	public ShipmentParcelDto(final PersonDto sender, final PersonDto recipient, final ShipmentSizeDto shipmentSize,
			final String destination, final ShipmentStatusDto shipmentStatus, final ParcelIdDto parcelRelatedId,
			final double price) {
		this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.destination = destination;
		this.shipmentStatus = shipmentStatus;
		this.parcelRelatedId = parcelRelatedId;
		this.price = price;
	}

    public PersonDto getSender() {
        return sender;
    }

    public PersonDto getRecipient() {
        return recipient;
    }

    public ShipmentSizeDto getShipmentSize() {
        return shipmentSize;
    }

    public String getDestination() {
        return destination;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public ParcelTypeDto getParcelType() {
        return parcelRelatedId != null && parcelRelatedId.getId() != null ? ParcelTypeDto.CHILD : ParcelTypeDto.PARENT;
    }

    public ParcelIdDto getParcelRelatedId() {
        return parcelRelatedId;
    }

    public double getPrice() {
        return price;
    }
}
