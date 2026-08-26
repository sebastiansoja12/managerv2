package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDateTime;

public class ShipmentDto {
    
    private final ShipmentIdDto shipmentId;

    private final PersonApi sender;

    private final PersonApi recipient;

    private final ShipmentSizeDto shipmentSize;

    private final DepartmentCodeDto destination;

    private final DepartmentId originDepartmentId;

    private final CountryCode originCountry;

    private final CountryCode destinationCountry;

    private final ShipmentStatusDto shipmentStatus;
    
    private final ShipmentIdDto shipmentRelatedId;

    private final ShipmentPriorityDto shipmentPriority;

    private final TrackingNumberDto trackingNumber;

    private final MoneyApi price;

    private final Boolean locked;

    private final SignatureDto signature;

    private final DangerousGoodApi dangerousGood;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

	public ShipmentDto(final ShipmentIdDto shipmentId, final PersonApi sender, final PersonApi recipient,
                       final ShipmentSizeDto shipmentSize, final DepartmentCodeDto destination,
                       final DepartmentId originDepartmentId, final CountryCode originCountry,
                       final CountryCode destinationCountry, final ShipmentStatusDto shipmentStatus,
                       final ShipmentIdDto shipmentRelatedId, final ShipmentPriorityDto shipmentPriority,
                       final TrackingNumberDto trackingNumber,
                       final MoneyApi price, final Boolean locked,
                       final SignatureDto signature, final DangerousGoodApi dangerousGood,
                       final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.shipmentId = shipmentId;
        this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.destination = destination;
        this.originDepartmentId = originDepartmentId;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
        this.shipmentPriority = shipmentPriority;
        this.trackingNumber = trackingNumber;
        this.price = price;
        this.locked = locked;
        this.signature = signature;
        this.dangerousGood = dangerousGood;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public DepartmentCodeDto getDestination() {
        return destination;
    }

    public DepartmentId getOriginDepartmentId() {
        return originDepartmentId;
    }

    public CountryCode getOriginCountry() {
        return originCountry;
    }

    public CountryCode getDestinationCountry() {
        return destinationCountry;
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

    public TrackingNumberDto getTrackingNumber() {
        return trackingNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
