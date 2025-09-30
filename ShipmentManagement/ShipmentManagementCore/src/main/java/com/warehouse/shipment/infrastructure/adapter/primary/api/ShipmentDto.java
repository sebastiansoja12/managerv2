package com.warehouse.shipment.infrastructure.adapter.primary.api;

public class ShipmentDto {
    
    private final ShipmentIdDto shipmentId;

    private final PersonApi sender;

    private final PersonApi recipient;

    private final ShipmentSizeDto shipmentSize;

    private final String destination;

    private final ShipmentStatusDto shipmentStatus;
    
    private final ShipmentIdDto shipmentRelatedId;

    private final ShipmentPriorityDto shipmentPriority;

    private final MoneyApi price;

    private final Boolean locked;

    private final SignatureDto signature;

    private final DangerousGoodApi dangerousGood;

	public ShipmentDto(final ShipmentIdDto shipmentId, final PersonApi sender, final PersonApi recipient,
                       final ShipmentSizeDto shipmentSize, final String destination, final ShipmentStatusDto shipmentStatus,
                       final ShipmentIdDto shipmentRelatedId, final ShipmentPriorityDto shipmentPriority,
                       final MoneyApi price, final Boolean locked,
                       final SignatureDto signature, final DangerousGoodApi dangerousGood) {
        this.shipmentId = shipmentId;
        this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.destination = destination;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
        this.shipmentPriority = shipmentPriority;
		this.price = price;
        this.locked = locked;
        this.signature = signature;
        this.dangerousGood = dangerousGood;
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

    public MoneyApi getPrice() {
        return price;
    }

    public ShipmentPriorityDto getShipmentPriority() {
        return shipmentPriority;
    }

    public SignatureDto getSignature() {
        return signature;
    }

    public DangerousGoodApi getDangerousGood() {
        return dangerousGood;
    }
}
