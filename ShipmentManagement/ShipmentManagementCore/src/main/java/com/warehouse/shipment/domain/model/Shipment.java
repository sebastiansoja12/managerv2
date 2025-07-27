package com.warehouse.shipment.domain.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.registry.DomainRegistry;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;


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


    public Shipment() {
        //
    }

	public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
                    final ShipmentStatus shipmentStatus,
                    final ShipmentType shipmentType,
                    final ShipmentId shipmentRelatedId,
                    final Money price,
                    final LocalDateTime createdAt,
                    final LocalDateTime updatedAt,
                    final Boolean locked,
                    final Country originCountry,
                    final Country destinationCountry,
                    final String destination,
                    final Signature signature,
                    final boolean signatureRequired,
                    final ShipmentPriority shipmentPriority) {
        this.shipmentId = shipmentId;
		this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
		this.shipmentType = shipmentType;
		this.price = price;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.locked = locked;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.destination = destination;
        this.signature = signature;
        this.signatureRequired = signatureRequired;
        this.shipmentPriority = shipmentPriority;
    }

    public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
                    final ShipmentId shipmentRelatedId,
                    final Country originCountry,
                    final Country destinationCountry,
                    final Money price,
                    final Boolean locked,
                    final String destination,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority) {
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.recipient = recipient;
        this.shipmentSize = shipmentSize;
        this.shipmentStatus = ShipmentStatus.CREATED;
        this.shipmentRelatedId = shipmentRelatedId;
        this.shipmentType = shipmentRelatedId != null ? ShipmentType.CHILD : ShipmentType.PARENT;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.locked = locked;
        this.signature = signature;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.destination = destination;
        this.signatureRequired = signature != null;
        this.shipmentPriority = shipmentPriority;
        DomainRegistry.publish(new ShipmentCreatedEvent(this.snapshot(), Instant.now()));
    }
    
    public Shipment(final Sender sender, 
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
                    final Money price,
                    final DangerousGood dangerousGood) {
        this.sender = sender;
        this.recipient = recipient;
        this.shipmentSize = shipmentSize;
        this.price = price;
        this.dangerousGood = dangerousGood;
        this.shipmentStatus = ShipmentStatus.CREATED;
        DomainRegistry.publish(new ShipmentCreatedEvent(this.snapshot(), Instant.now()));
    }

    private ShipmentSnapshot snapshot() {
        return new ShipmentSnapshot(shipmentId, sender, recipient, shipmentStatus);
    }

    public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentUpdateType updateType) {
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.recipient = recipient;
        this.shipmentStatus = determineShipmentStatus(updateType);
        DomainRegistry.publish(new ShipmentCreatedEvent(this.snapshot(), Instant.now()));
    }

    private ShipmentStatus determineShipmentStatus(final ShipmentUpdateType updateType) {
        return switch (updateType) {
            case REROUTE -> ShipmentStatus.REROUTE;
            case REDIRECT -> ShipmentStatus.REDIRECT;
        };
    }

    public static Shipment from(final ShipmentCreateRequest request) {
		return new Shipment(request.getSender(), request.getRecipient(), request.getShipmentSize(), request.getPrice(),
				request.getDangerousGood());
    }

    public static Shipment from(final ShipmentUpdateRequest request) {
        final ShipmentId shipmentId = request.getShipmentId();
        final ShipmentUpdate shipmentUpdate = request.getShipmentUpdate();
        return new Shipment(shipmentId, shipmentUpdate.getSender(), shipmentUpdate.getRecipient(), request.getShipmentUpdateType());
    }

    public static Shipment from(final ShipmentEntity shipmentEntity) {
        final ShipmentId shipmentId = shipmentEntity.getShipmentId();
        final Sender sender = Sender.from(shipmentEntity);
        final Recipient recipient = Recipient.from(shipmentEntity);
        final ShipmentSize shipmentSize = shipmentEntity.getShipmentSize();
        final ShipmentStatus shipmentStatus = shipmentEntity.getShipmentStatus();
        final ShipmentId shipmentRelatedId = shipmentEntity.getShipmentRelatedId();
        final ShipmentType shipmentType = shipmentEntity.getShipmentType();
        final Money price = shipmentEntity.getPrice();
        final LocalDateTime createdAt = shipmentEntity.getCreatedAt();
        final LocalDateTime updatedAt = shipmentEntity.getUpdatedAt();
        final Boolean locked = shipmentEntity.getLocked();
        final Country originCountry = shipmentEntity.getOriginCountry();
        final Country destinationCountry = shipmentEntity.getDestinationCountry();
        final String destination = shipmentEntity.getDestination();
        final Signature signature = null;
        final boolean signatureRequired = false;
        final ShipmentPriority shipmentPriority = shipmentEntity.getShipmentPriority();

        return new Shipment(
                shipmentId,
                sender,
                recipient,
                shipmentSize,
                shipmentStatus,
                shipmentType,
                shipmentRelatedId,
                price,
                createdAt,
                updatedAt,
                locked,
                originCountry,
                destinationCountry,
                destination,
                signature,
                signatureRequired,
                shipmentPriority
        );
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

    public Boolean getLocked() {
        return locked;
    }

    public DangerousGood getDangerousGood() {
        return dangerousGood;
    }

    public Boolean getSignatureRequired() {
        return signatureRequired;
    }

    public ShipmentPriority getShipmentPriority() {
        return shipmentPriority;
    }

    public Country getOriginCountry() {
        return originCountry;
    }

    public Country getDestinationCountry() {
        return destinationCountry;
    }

    public void changeSignature(final Signature signature) {
        this.signature = signature;
        markAsModified();
    }

    public void prepareShipmentToCreate() {
        this.shipmentStatus = ShipmentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
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
        DomainRegistry.publish(new ShipmentChangedEvent(snapshot(), Instant.now()));
    }

    public void lockShipment() {
        this.locked = true;
    }

    public void prepareShipmentToSend() {
        this.shipmentStatus = ShipmentStatus.SENT;
        markAsModified();
    }

    public void prepareShipmentToDeliver() {
        changeShipmentStatus(ShipmentStatus.DELIVERY);
        markAsModified();
    }

    public void changeSender(final Sender sender) {
        this.sender = sender;
        markAsModified();
        DomainRegistry.publish(new ShipmentSenderChanged(snapshot(), Instant.now()));
    }

    public void changeRecipient(final Recipient recipient) {
        this.recipient = recipient;
        markAsModified();
        DomainRegistry.publish(new ShipmentRecipientChanged(snapshot(), Instant.now()));
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

    public void updateDestination(final VoronoiResponse voronoiResponse) {
        if (ObjectUtils.isNotEmpty(voronoiResponse) && voronoiResponse.getValue() != null) {
            this.destination = voronoiResponse.getValue();
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
        DomainRegistry.publish(new ShipmentStatusChangedEvent(snapshot(), Instant.now()));
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
        changeShipmentStatus(ShipmentStatus.SENT);
        this.shipmentType = ShipmentType.PARENT;
        this.shipmentRelatedId = null;
        unlockShipment();
        markAsModified();
    }

    public void notifyShipmentRerouted() {
        changeShipmentStatus(ShipmentStatus.REROUTE);
        markAsModified();
    }

    public void notifyShipmentSent() {
        changeShipmentStatus(ShipmentStatus.SENT);
        markAsModified();
    }

    public void notifyShipmentReturned() {
        changeShipmentStatus(ShipmentStatus.RETURN);
        markAsModified();
    }

    public void notifyShipmentDelivered() {
        changeShipmentStatus(ShipmentStatus.DELIVERY);
        markAsModified();
    }

    public void changeShipmentCountries(final ShipmentCountry shipmentCountry) {
        this.originCountry = shipmentCountry.originCountry();
        this.destinationCountry = shipmentCountry.destinationCountry();
    }

    public void changeDestinationDepartment(final String destination) {
        this.destination = destination;
    }

    private void unlockShipment() {
        this.locked = false;
    }

    public void setDangerousGood(final DangerousGood dangerousGood) {
        this.dangerousGood = dangerousGood;
    }

    public void setDestinationCountry(final Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public void setOriginCountry(final Country originCountry) {
        this.originCountry = originCountry;
    }

    public void setShipmentPriority(final ShipmentPriority shipmentPriority) {
        this.shipmentPriority = shipmentPriority;
    }

    public void setShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void setSignature(final Signature signature) {
        this.signature = signature;
    }

    public void setSignatureRequired(final Boolean signatureRequired) {
        this.signatureRequired = signatureRequired;
    }

    public boolean validateShipmentPrice() {
        return this.price == null;
    }
}
