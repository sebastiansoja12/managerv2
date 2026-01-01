package com.warehouse.shipment.domain.model;

import java.time.Instant;
import java.time.LocalDateTime;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.event.ShipmentChangedEvent;
import com.warehouse.shipment.domain.event.ShipmentCountriesChanged;
import com.warehouse.shipment.domain.registry.DomainContext;
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

    private CountryCode originCountry;

    private CountryCode destinationCountry;

    private Signature signature;

    private ExternalId<String> externalRouteId;

    private ExternalId<Long> externalReturnId;

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
                    final CountryCode originCountry,
                    final CountryCode destinationCountry,
                    final String destination,
                    final Signature signature,
                    final boolean signatureRequired,
                    final ShipmentPriority shipmentPriority,
                    final DangerousGood dangerousGood,
                    final ExternalId<String> externalRouteId,
                    final ExternalId<Long> externalReturnId) {
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
        this.dangerousGood = dangerousGood;
        this.externalRouteId = externalRouteId;
        this.externalReturnId = externalReturnId;
    }

    public Shipment(final ShipmentId shipmentId,
                    final Sender sender,
                    final Recipient recipient,
                    final ShipmentSize shipmentSize,
                    final ShipmentId shipmentRelatedId,
                    final CountryCode originCountry,
                    final CountryCode destinationCountry,
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
    }

	public ShipmentSnapshot snapshot() {
		return new ShipmentSnapshot(shipmentId, sender, recipient, shipmentSize, destination, shipmentStatus,
				shipmentType, shipmentRelatedId, price, createdAt, updatedAt, locked, dangerousGood, signatureRequired,
				shipmentPriority, originCountry, destinationCountry, signature, externalRouteId, externalReturnId);
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
        final CountryCode originCountry = shipmentEntity.getOriginCountry();
        final CountryCode destinationCountry = shipmentEntity.getDestinationCountry();
        final String destination = shipmentEntity.getDestination();
        final Signature signature = shipmentEntity.getSignature() != null ? Signature.from(shipmentEntity.getSignature()) : null;
        final boolean signatureRequired = signature != null;
        final ShipmentPriority shipmentPriority = shipmentEntity.getShipmentPriority();
        final DangerousGood dangerousGood = shipmentEntity.getDangerousGood() != null ? DangerousGood.from(shipmentEntity.getDangerousGood()) : null;
        final ExternalId<String> externalRouteId = shipmentEntity.getExternalRouteId();
        final ExternalId<Long> externalReturnId = shipmentEntity.getExternalReturnId();

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
                shipmentPriority,
                dangerousGood,
                externalRouteId,
                externalReturnId
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

    public CountryCode getOriginCountry() {
        return originCountry;
    }

    public CountryCode getDestinationCountry() {
        return destinationCountry;
    }

    public ExternalId<Long> getExternalReturnId() {
        return externalReturnId;
    }

    public void setExternalReturnId(final ExternalId<Long> externalReturnId) {
        this.externalReturnId = externalReturnId;
    }

    public ExternalId<String> getExternalRouteId() {
        return externalRouteId;
    }

    public void setExternalRouteId(final ExternalId<String> externalRouteId) {
        this.externalRouteId = externalRouteId;
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
        DomainContext.publish(new ShipmentChangedEvent(snapshot(), Instant.now()));
    }

    public void lockShipment() {
        this.locked = true;
        markAsModified();
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

    public void update(final Sender sender, final Recipient recipient, final ShipmentStatus shipmentStatus,
                       final ShipmentPriority shipmentPriority, final ShipmentSize shipmentSize,
                       final Money price, final DangerousGood dangerousGood,
                       final String destination, final Boolean signatureRequired) {
        this.recipient = recipient;
        this.sender = sender;
        this.shipmentStatus = shipmentStatus;
        this.shipmentPriority = shipmentPriority;
        this.shipmentSize = shipmentSize;
        this.price = price;
        if (dangerousGood != null) {
            this.dangerousGood = dangerousGood;
        }
        this.destination = destination;
        this.signatureRequired = signatureRequired;
        markAsModified();
    }

    public void changeShipmentType(final ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = null;
        this.locked = false;
        markAsModified();
    }

    public void changeShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void notifyRelatedShipmentRedirected(final ShipmentId relatedShipmentId) {
        this.shipmentRelatedId = relatedShipmentId;
        this.shipmentType = ShipmentType.CHILD;
        this.shipmentStatus = ShipmentStatus.REDIRECT;
        lockShipment();
        markAsModified();
    }

    public void changeCurrency(final Currency currency) {
        this.price.changeCurrency(currency);
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
        lockShipment();
        markAsModified();
    }

    public void notifyShipmentReturnCanceled() {
        changeShipmentStatus(ShipmentStatus.DELIVERY);
        this.locked = false;
        markAsModified();
    }

    public void notifyShipmentReturnCreated(final ExternalId<String> externalRouteId,
                                            final ExternalId<Long> externalReturnId) {
        this.externalRouteId = externalRouteId;
        this.externalReturnId = externalReturnId;
        markAsModified();
    }

    public void changeDestinationDepartment(final String destination) {
        this.destination = destination;
        markAsModified();
    }

    private void unlockShipment() {
        this.locked = false;
    }

    public void setDangerousGood(final DangerousGood dangerousGood) {
        this.dangerousGood = dangerousGood;
    }

    public void setDestinationCountry(final CountryCode destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public void setOriginCountry(final CountryCode originCountry) {
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

    public void updateCountries(final ShipmentCountryRequest request) {
        markAsModified();
        DomainContext.publish(new ShipmentCountriesChanged(this.snapshot(), Instant.now()));
    }

    public void changeIssuerCountry(final CountryCode originCountry) {
        this.originCountry = originCountry;
        markAsModified();
        DomainContext.publish(new ShipmentCountriesChanged(this.snapshot(), Instant.now()));
    }

    public void changeReceiverCountry(final CountryCode destinationCountry) {
        this.destinationCountry = destinationCountry;
        markAsModified();
        DomainContext.publish(new ShipmentCountriesChanged(this.snapshot(), Instant.now()));
    }

    public void changeShipmentTypeWithRelatedId(final ShipmentType shipmentType, final ShipmentId relatedShipmentId) {
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = relatedShipmentId;
        this.locked = true;
        markAsModified();
    }

    public void lockShipmentWithShipmentType(final ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
        lockShipment();
    }

    public void assignRouteProcessId(final ProcessId processId) {
        this.externalRouteId = new ExternalId<>(processId.getValue().toString());
        markAsModified();
    }

    public void assignReturnId(final ReturnId returnId) {
        this.externalReturnId = new ExternalId<>(returnId.getId());
        markAsModified();
    }

    public boolean isFullyDelivered() {
        return isLocked() && ShipmentStatus.DELIVERY.equals(this.shipmentStatus);
    }

    public Shipment redirectToSender(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
        this.shipmentType = ShipmentType.PARENT;

        final Sender newSender = new Sender(
                recipient.getFirstName(),
                recipient.getLastName(),
                recipient.getEmail(),
                recipient.getTelephoneNumber(),
                recipient.getCity(),
                recipient.getPostalCode(),
                recipient.getStreet()
        );

        final Recipient newRecipient = new Recipient(
                sender.getFirstName(),
                sender.getLastName(),
                sender.getEmail(),
                sender.getTelephoneNumber(),
                sender.getCity(),
                sender.getPostalCode(),
                sender.getStreet()
        );

        this.sender = newSender;
        this.recipient = newRecipient;

        this.shipmentStatus = ShipmentStatus.CREATED;

        markAsModified();

        return this;
    }

}
