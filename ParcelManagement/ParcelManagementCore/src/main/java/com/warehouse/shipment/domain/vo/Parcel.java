package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ParcelType;
import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.model.Recipient;
import com.warehouse.shipment.domain.model.Sender;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentSize;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentStatus;
import lombok.Builder;


@Builder
public class Parcel {

    private final ParcelId id;

    private final Sender sender;

    private final Recipient recipient;

    private final ShipmentSize parcelShipmentSize;

    private final String destination;

    private final ShipmentStatus parcelShipmentStatus;

    private final ParcelType parcelType;

    private final ParcelId parcelRelatedId;

    private final double price;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean locked;

    public Parcel(ParcelId id, Sender sender, Recipient recipient, ShipmentSize parcelShipmentSize, String destination,
                  ShipmentStatus parcelShipmentStatus, ParcelType parcelType, ParcelId parcelRelatedId, double price, LocalDateTime createdAt,
                  LocalDateTime updatedAt, Boolean locked) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.parcelShipmentSize = parcelShipmentSize;
        this.destination = destination;
        this.parcelShipmentStatus = parcelShipmentStatus;
        this.parcelType = parcelType;
        this.parcelRelatedId = parcelRelatedId;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locked = locked;
    }

    public ParcelId getId() {
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

    public ParcelType getParcelType() {
        return parcelType;
    }

    public ParcelId getParcelRelatedId() {
        return parcelRelatedId;
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
