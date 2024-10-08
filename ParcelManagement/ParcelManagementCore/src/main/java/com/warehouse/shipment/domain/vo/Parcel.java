package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;


@Builder
public class Parcel {

    private final ShipmentId id;

    private final Sender sender;

    private final Recipient recipient;

    private final ShipmentSize parcelShipmentSize;

    private final String destination;

    private final ShipmentStatus parcelShipmentStatus;

    private final ShipmentType shipmentType;

    private final ShipmentId shipmentRelatedId;

    private final double price;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean locked;

	public Parcel(final ShipmentId id, final Sender sender, final Recipient recipient,
                  final ShipmentSize parcelShipmentSize, final String destination, final ShipmentStatus parcelShipmentStatus,
                  final ShipmentType shipmentType, final ShipmentId shipmentRelatedId, final double price,
                  final LocalDateTime createdAt, final LocalDateTime updatedAt, final Boolean locked) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.parcelShipmentSize = parcelShipmentSize;
        this.destination = destination;
        this.parcelShipmentStatus = parcelShipmentStatus;
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = shipmentRelatedId;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locked = locked;
    }

    public ShipmentId getId() {
        return id;
    }

    public Sender getSender() {
        return sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public ShipmentSize getParcelShipmentSize() {
        return parcelShipmentSize;
    }

    public String getDestination() {
        return destination;
    }

    public ShipmentStatus getParcelShipmentStatus() {
        return parcelShipmentStatus;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public ShipmentId getShipmentRelatedId() {
        return shipmentRelatedId;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getLocked() {
        return locked;
    }
}
