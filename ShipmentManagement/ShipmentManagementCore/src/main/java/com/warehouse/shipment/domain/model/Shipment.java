package com.warehouse.shipment.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentRequest;


public class Shipment {

    private ShipmentId shipmentId;

    private Sender sender;

    private Recipient recipient;

    private ShipmentSize shipmentSize;

    private String destination;

    private ShipmentStatus shipmentStatus;

    private ShipmentType shipmentType;

    private ShipmentId shipmentRelatedId;

    private Money price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean locked;

    private DangerousGood dangerousGood;

    private Boolean signatureRequired;

    private ShipmentPriority shipmentPriority;

    private Country originCountry;

    private Country destinationCountry;

    private Signature signature;

	public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
                    final ShipmentStatus shipmentStatus,
                    final ShipmentId shipmentRelatedId,
                    final Money price,
                    final LocalDateTime createdAt,
                    final LocalDateTime updatedAt,
                    final Boolean locked,
                    final Signature signature) {
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
        this.signature = signature;
    }

    public static Shipment from(final ShipmentRequest request) {
        return request.getShipment();
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

    public void setPrice(final Money price) {
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

    public Money getPrice() {
        return price;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Signature getSignature() {
        return signature;
    }

    public void changeSignature(final Signature signature) {
        this.signature = signature;
        markAsModified();
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

    public void prepareShipmentToRedirect(final ShipmentId newRelatedShipmentId) {
        this.shipmentStatus = ShipmentStatus.REDIRECT;
        this.shipmentType = ShipmentType.CHILD;
        this.shipmentRelatedId = newRelatedShipmentId;
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
        markAsModified();
    }

    public void changeRecipient(final Recipient recipient) {
        this.recipient = recipient;
        markAsModified();
    }

    public void changeShipmentSize(final ShipmentSize shipmentSize) {
        this.shipmentSize = shipmentSize;
        markAsModified();
    }

    public void changePrice(final Money price) {
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
        if (Objects.isNull(this.shipmentRelatedId)) {
            throw new RuntimeException("Shipment type cannot be changed");
        }
        this.shipmentType = shipmentType;
        markAsModified();
    }

    public void changeShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
        markAsModified();
    }

    public void notifyRelatedShipmentRedirected(final ShipmentId relatedShipmentId) {
        this.shipmentRelatedId = relatedShipmentId;
        this.shipmentType = ShipmentType.CHILD;
        lockShipment();
        markAsModified();
    }

    public void changeCurrency(final Currency currency) {
        this.price.changeCurrency(currency);
        markAsModified();
    }

    public void changeShipmentOrigin(final Country originCountry) {
        this.originCountry = originCountry;
        markAsModified();
    }

    public void changeShipmentDestinationCountry(final Country destinationCountry) {
        this.destinationCountry = destinationCountry;
        markAsModified();
    }

    public void changeSignatureRequired(final boolean signatureRequired) {
        this.signatureRequired = signatureRequired;
        markAsModified();
    }

    public void changeDangerousGood(final DangerousGood dangerousGood) {
        this.dangerousGood = dangerousGood;
        markAsModified();
    }

    public void changeShipmentPriority(final ShipmentPriority shipmentPriority) {
        this.shipmentPriority = shipmentPriority;
        markAsModified();
    }

    public void changeShipmentRelatedId(final ShipmentId relatedShipmentId) {
        this.shipmentRelatedId = relatedShipmentId;
        markAsModified();
    }

    public void notifyRelatedShipmentLocked() {
        this.shipmentStatus = ShipmentStatus.SENT;
        this.shipmentType = ShipmentType.PARENT;
        this.shipmentRelatedId = null;
        unlockShipment();
        markAsModified();
    }

    public void notifyShipmentRerouted() {
        this.shipmentStatus = ShipmentStatus.REROUTE;
        markAsModified();
    }

    public void notifyShipmentSent() {
        this.shipmentStatus = ShipmentStatus.SENT;
        markAsModified();
    }

    public void notifyShipmentReturned() {
        this.shipmentStatus = ShipmentStatus.RETURN;
        markAsModified();
    }

    public void notifyShipmentDelivered() {
        this.shipmentStatus = ShipmentStatus.DELIVERY;
        markAsModified();
    }

    private void unlockShipment() {
        this.locked = false;
    }

    public Shipment snapshot() {
        return new Shipment(
                this.shipmentId,
                this.sender,
                this.recipient,
                this.shipmentSize,
                this.shipmentStatus,
                this.shipmentRelatedId,
                this.price,
                this.createdAt,
                this.updatedAt,
                this.locked,
                this.signature
        );
    }

}
