package com.warehouse.shipment.infrastructure.api.dto;

public class ShipmentDto {
    
    private final ShipmentIdDto shipmentId;

    private final PersonDto sender;

    private final PersonDto recipient;

    private final ShipmentSizeDto shipmentSize;

    private final String destination;

    private final ShipmentStatusDto shipmentStatus;
    
    private final ShipmentIdDto shipmentRelatedId;

    private final MoneyDto price;

    private final Boolean locked;

	public ShipmentDto(final ShipmentIdDto shipmentId, final PersonDto sender, final PersonDto recipient,
                       final ShipmentSizeDto shipmentSize, final String destination, final ShipmentStatusDto shipmentStatus,
                       final ShipmentIdDto shipmentRelatedId, final MoneyDto price, final Boolean locked) {
        this.shipmentId = shipmentId;
        this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.destination = destination;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
		this.price = price;
        this.locked = locked;
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

    public ShipmentSizeDto getShipmentSize() {
        return shipmentSize;
    }

    public String getDestination() {
        return destination;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public Boolean getLocked() {
        return locked;
    }

    public ShipmentTypeDto getShipmentType() {
        return shipmentRelatedId != null && shipmentRelatedId.getValue() != null ? ShipmentTypeDto.CHILD : ShipmentTypeDto.PARENT;
    }

    public ShipmentIdDto getShipmentRelatedId() {
        return shipmentRelatedId;
    }

    public MoneyDto getPrice() {
        return price;
    }
}
