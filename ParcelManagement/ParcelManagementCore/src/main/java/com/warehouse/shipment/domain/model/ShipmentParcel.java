package com.warehouse.shipment.domain.model;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ParcelType;
import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentSize;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentStatus;


public class ShipmentParcel {

    private Sender sender;

    private Recipient recipient;

    private ShipmentSize parcelShipmentSize;

    private String destination;

    private ShipmentStatus shipmentStatus;

    private ParcelType parcelType;

    private ParcelId parcelRelatedId;

    private double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean locked;

	public ShipmentParcel(final Sender sender, final Recipient recipient, final ShipmentSize parcelShipmentSize,
			final ShipmentStatus shipmentStatus, final ParcelId parcelRelatedId, final double price,
			final LocalDateTime createdAt, final LocalDateTime updatedAt, final Boolean locked) {
		this.sender = sender;
		this.recipient = recipient;
		this.parcelShipmentSize = parcelShipmentSize;
		this.shipmentStatus = shipmentStatus;
		this.parcelRelatedId = parcelRelatedId;
		this.parcelType = parcelRelatedId != null ? ParcelType.CHILD : ParcelType.PARENT;
		this.price = price;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.locked = locked;
	}

    public Sender getSender() {
        return sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public ShipmentSize getShipmentSize() {
        return parcelShipmentSize;
    }

    public String getDestination() {
        return destination;
    }

    public ShipmentStatus getStatus() {
        return shipmentStatus;
    }

    public ParcelType getParcelType() {
        return parcelType;
    }

    public ParcelId getParcelRelatedId() {
        return parcelRelatedId;
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

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public void setParcelSize(ShipmentSize parcelShipmentSize) {
        this.parcelShipmentSize = parcelShipmentSize;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public void setParcelRelatedId(ParcelId parcelRelatedId) {
        this.parcelRelatedId = parcelRelatedId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public double getPrice() {
        return this.price = parcelShipmentSize.getPrice();
    }

    public void prepareParcelToCreate() {
        this.shipmentStatus = ShipmentStatus.CREATED;
        this.parcelType = ParcelType.PARENT;
    }

    public void updateDestination(final String destination) {
        this.destination = destination;
    }
}
