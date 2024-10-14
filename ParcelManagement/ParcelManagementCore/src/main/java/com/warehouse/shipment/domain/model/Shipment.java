package com.warehouse.shipment.domain.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;


public class Shipment {

    private ShipmentId shipmentId;

    private Sender sender;

    private Recipient recipient;

    private ShipmentSize shipmentSize;

    private String destination;

    private ShipmentStatus shipmentStatus;

    private ShipmentType shipmentType;

    private ShipmentId shipmentRelatedId;

    private double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean locked;

	public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
			        final ShipmentStatus shipmentStatus,
                    final ShipmentId shipmentRelatedId,
                    final double price,
                    final LocalDateTime createdAt,
                    final LocalDateTime updatedAt,
                    final Boolean locked) {
        this.shipmentId = shipmentId;
		this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
		this.shipmentType = shipmentRelatedId != null ? ShipmentType.CHILD : ShipmentType.PARENT;
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
        return shipmentSize;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setSender(final Sender sender) {
        this.sender = sender;
    }

    public void setRecipient(final Recipient recipient) {
        this.recipient = recipient;
    }

    public void setShipmentSize(final ShipmentSize parcelShipmentSize) {
        this.shipmentSize = parcelShipmentSize;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public void setStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void setShipmentType(final ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public void setShipmentRelatedId(final ShipmentId shipmentRelatedId) {
        this.shipmentRelatedId = shipmentRelatedId;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLocked(final Boolean locked) {
        this.locked = locked;
    }

    public double getPrice() {
        return price;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void prepareShipmentToCreate() {
        this.shipmentStatus = ShipmentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        markAsModified();
    }

    public void prepareShipmentToReroute() {
        this.shipmentStatus = ShipmentStatus.REROUTE;
        markAsModified();
    }

    public void prepareShipmentToRedirect(final ShipmentId newShipmentId) {
        this.shipmentStatus = ShipmentStatus.REDIRECT;
        this.shipmentType = ShipmentType.CHILD;
        this.shipmentRelatedId = newShipmentId;
        markAsModified();
        lockShipment();
    }

    public void lockShipment() {
        this.locked = true;
    }

    public void prepareShipmentToSend() {
        this.shipmentStatus = ShipmentStatus.SENT;
        markAsModified();
    }

    public void prepareShipmentToDeliver() {
        this.shipmentStatus = ShipmentStatus.DELIVERY;
        markAsModified();
    }

    public void changeSender(final Sender sender) {
        this.sender = sender;
    }

    public void changeRecipient(final Recipient recipient) {
        this.recipient = recipient;
    }

    public void changeShipmentSize(final ShipmentSize shipmentSize) {
        this.shipmentSize = shipmentSize;
        markAsModified();
    }

    public void changePrice(final double price) {
        this.price = price;
        markAsModified();
    }

    public void markAsModified() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDestination(final City city) {
        if (ObjectUtils.isNotEmpty(city) && city.getValue() != null) {
            this.destination = city.getValue();
            markAsModified();
        }
    }

    public void update(final ShipmentUpdate shipmentUpdate) {
        this.recipient = shipmentUpdate.getRecipient();
        this.sender = shipmentUpdate.getSender();
        markAsModified();
    }

    public void changeShipmentType(final ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }
}
