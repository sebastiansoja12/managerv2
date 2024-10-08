package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;


@Builder
public class Parcel {

    private final ShipmentId shipmentId;

    private final Sender sender;

    private final Recipient recipient;

    private final ShipmentSize parcelShipmentSize;

    private final String destination;

    private final ShipmentStatus shipmentStatus;

    private final ShipmentType shipmentType;

    private final ShipmentId shipmentRelatedId;

    private final double price;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean locked;

	public Parcel(final ShipmentId shipmentId, final Sender sender, final Recipient recipient,
                  final ShipmentSize parcelShipmentSize, final String destination, final ShipmentStatus shipmentStatus,
                  final ShipmentType shipmentType, final ShipmentId shipmentRelatedId, final double price,
                  final LocalDateTime createdAt, final LocalDateTime updatedAt, final Boolean locked) {
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.recipient = recipient;
        this.parcelShipmentSize = parcelShipmentSize;
        this.destination = destination;
        this.shipmentStatus = shipmentStatus;
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = shipmentRelatedId;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locked = locked;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
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

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
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
