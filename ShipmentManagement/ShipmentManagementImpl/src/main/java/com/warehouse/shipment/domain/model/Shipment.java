package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.registry.DomainContext;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Shipment {

    private ShipmentId shipmentId;

    private Sender sender;

    private Recipient recipient;

    private ShipmentSize shipmentSize;

    private DepartmentCode destination;

    private DepartmentId originDepartmentId;

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

    private TrackingNumber trackingNumber;

    private ExternalId<UUID> externalShipmentId;

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
                    final DepartmentCode destination,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final boolean signatureRequired,
                    final ShipmentPriority shipmentPriority,
                    final DangerousGood dangerousGood,
                    final TrackingNumber trackingNumber,
                    final ExternalId<UUID> externalShipmentId) {
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
        this.originDepartmentId = originDepartmentId;
        this.signature = signature;
        this.signatureRequired = signatureRequired;
        this.shipmentPriority = shipmentPriority;
        this.dangerousGood = dangerousGood;
        this.trackingNumber = trackingNumber;
        this.externalShipmentId = externalShipmentId;
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
                    final DepartmentCode destination,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status) {
        this(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, locked, destination, null, signature, shipmentPriority, trackingNumber, status);
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
                    final DepartmentCode destination,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status) {
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.recipient = recipient;
        this.shipmentSize = shipmentSize;
        this.shipmentStatus = status;
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
        this.originDepartmentId = originDepartmentId;
        this.signatureRequired = signature != null;
        this.shipmentPriority = shipmentPriority;
        this.trackingNumber = trackingNumber;
        this.externalShipmentId = ExternalId.randomUUID();
    }

    public static Shipment parentShipment(final ShipmentId shipmentId,
                                          final Sender sender,
                                          final Recipient recipient,
                                          final ShipmentSize shipmentSize,
                                          final ShipmentId shipmentRelatedId,
                                          final CountryCode originCountry,
                                          final CountryCode destinationCountry,
                                          final Money price,
                                          final DepartmentCode destination,
                                          final DepartmentId originDepartmentId,
                                          final Signature signature,
                                          final ShipmentPriority shipmentPriority,
                                          final TrackingNumber trackingNumber,
                                          final ShipmentStatus status) {
        return new Shipment(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, false, destination, originDepartmentId, signature, shipmentPriority, trackingNumber, status);
	}

	public ShipmentSnapshot snapshot() {
		return new ShipmentSnapshot(shipmentId, sender, recipient, shipmentSize, destination, originDepartmentId, shipmentStatus,
				shipmentType, shipmentRelatedId, price, createdAt, updatedAt, locked, dangerousGood, signatureRequired,
				shipmentPriority, originCountry, destinationCountry, signature,
                trackingNumber, externalShipmentId);
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
        final DepartmentCode destination = shipmentEntity.getDestination();
        final DepartmentId originDepartmentId = shipmentEntity.getOriginDepartmentId();
        final Signature signature = shipmentEntity.getSignature() != null ? Signature.from(shipmentEntity.getSignature()) : null;
        final boolean signatureRequired = signature != null;
        final ShipmentPriority shipmentPriority = shipmentEntity.getShipmentPriority();
        final DangerousGood dangerousGood = shipmentEntity.getDangerousGood() != null
                ? shipmentEntity.getDangerousGood().toDomain()
                : null;
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
                originDepartmentId,
                signature,
                signatureRequired,
                shipmentPriority,
                dangerousGood,
                shipmentEntity.getTrackingNumber(),
                new ExternalId<>(UUID.fromString(shipmentEntity.getExternalId().value()))
        );
    }

    public static Shipment from(final ShipmentReadEntity shipmentEntity) {
        final ShipmentId shipmentId = shipmentEntity.getShipmentId();
        final Sender sender = new Sender(shipmentEntity.getFirstName(), shipmentEntity.getLastName(),
                shipmentEntity.getSenderEmail(), shipmentEntity.getSenderTelephone(), shipmentEntity.getSenderCity(),
                shipmentEntity.getSenderPostalCode(), shipmentEntity.getSenderStreet());
        final Recipient recipient = new Recipient(shipmentEntity.getRecipientFirstName(),
                shipmentEntity.getRecipientLastName(), shipmentEntity.getRecipientEmail(),
                shipmentEntity.getRecipientTelephone(), shipmentEntity.getRecipientCity(),
                shipmentEntity.getRecipientPostalCode(), shipmentEntity.getRecipientStreet());
        final Signature signature = shipmentEntity.getSignature() != null ? Signature.from(shipmentEntity.getSignature()) : null;
        final boolean signatureRequired = signature != null;
        final DangerousGood dangerousGood = shipmentEntity.dangerousGood();

        return new Shipment(
                shipmentId,
                sender,
                recipient,
                shipmentEntity.getShipmentSize(),
                shipmentEntity.getShipmentStatus(),
                shipmentEntity.getShipmentType(),
                shipmentEntity.getShipmentRelatedId(),
                shipmentEntity.getPrice(),
                shipmentEntity.getCreatedAt(),
                shipmentEntity.getUpdatedAt(),
                shipmentEntity.getLocked(),
                shipmentEntity.getOriginCountry(),
                shipmentEntity.getDestinationCountry(),
                shipmentEntity.getDestination(),
                shipmentEntity.getOriginDepartmentId(),
                signature,
                signatureRequired,
                shipmentEntity.getShipmentPriority(),
                dangerousGood,
                shipmentEntity.getTrackingNumber(),
                new ExternalId<>(UUID.fromString(shipmentEntity.getExternalId().value()))
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

    public DepartmentCode getDestination() {
        return destination;
    }

    public DepartmentId getOriginDepartmentId() {
        return originDepartmentId;
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

    public void setDestination(final DepartmentCode destination) {
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

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public ExternalId<UUID> getExternalShipmentId() {
        return externalShipmentId;
    }

    public void changeSignature(final Signature signature) {
        ensureShipmentIsNotDelivered();
        this.signature = signature;
        markAsModified();
    }

    public void prepareShipmentToCreate() {
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public void prepareShipmentToReroute() {
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.REROUTE;
        markAsModified();
    }

    public void prepareShipmentToRedirect(final ShipmentId newRelatedShipmentId) {
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.REDIRECT;
        this.shipmentType = ShipmentType.CHILD;
        this.shipmentRelatedId = newRelatedShipmentId;
        markAsModified();
        lockShipment();
    }

    public void lockShipment() {
        this.locked = true;
        markAsModified();
    }

    public void prepareShipmentToSend() {
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.SENT;
        markAsModified();
    }

    public void prepareShipmentToDeliver() {
        changeShipmentStatus(ShipmentStatus.DELIVERY);
        markAsModified();
    }

    public void changeSender(final Sender sender) {
        ensureCanBeModified();
        this.sender = sender;
        markAsModified();
    }

    public void changeRecipient(final Recipient recipient) {
        ensureCanBeModified();
        this.recipient = recipient;
        markAsModified();
    }

    public void changeShipmentSize(final ShipmentSize shipmentSize) {
        ensureCanBeModified();
        this.shipmentSize = shipmentSize;
        markAsModified();
    }

    public void changePrice(final Money price) {
        ensureCanBeModified();
        this.price = price;
        markAsModified();
    }

    public void markAsModified() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDestination(final VoronoiResponse voronoiResponse) {
        ensureCanBeModified();
        if (ObjectUtils.isNotEmpty(voronoiResponse) && voronoiResponse.getDepartmentCodeResult() != null) {
            this.destination = voronoiResponse.getDepartmentCodeResult();
            markAsModified();
        }
    }

    public void update(final ShipmentUpdate shipmentUpdate) {
        ensureCanBeModified();
        this.recipient = shipmentUpdate.getRecipient();
        this.sender = shipmentUpdate.getSender();
        markAsModified();
    }

    public void update(final Sender sender, final Recipient recipient, final ShipmentStatus shipmentStatus,
                       final ShipmentPriority shipmentPriority, final ShipmentSize shipmentSize,
                       final Money price, final DangerousGood dangerousGood,
                       final DepartmentCode destination, final Boolean signatureRequired) {
        ensureCanBeModified();
        this.recipient = recipient;
        this.sender = sender;
        this.shipmentStatus = shipmentStatus;
        this.shipmentPriority = shipmentPriority;
        this.shipmentSize = shipmentSize;
        this.price = price;
        this.dangerousGood = dangerousGood;
        this.destination = destination;
        this.signatureRequired = signatureRequired;
        markAsModified();
    }

    public void changeShipmentType(final ShipmentType shipmentType) {
        ensureShipmentIsNotDelivered();
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = null;
        this.locked = false;
        markAsModified();
    }

    public void changeShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void notifyRelatedShipmentRedirected(final ShipmentId relatedShipmentId) {
        ensureShipmentIsNotDelivered();
        this.shipmentRelatedId = relatedShipmentId;
        this.shipmentType = ShipmentType.CHILD;
        this.shipmentStatus = ShipmentStatus.REDIRECT;
        lockShipment();
        markAsModified();
    }

    public void changeCurrency(final Currency currency) {
        ensureCanBeModified();
        this.price.changeCurrency(currency);
        markAsModified();
    }

    public void changeSignatureRequired(final boolean signatureRequired) {
        ensureCanBeModified();
        this.signatureRequired = signatureRequired;
        markAsModified();
    }

    public void changeDangerousGood(final DangerousGood dangerousGood) {
        if (Objects.equals(this.dangerousGood, dangerousGood)) {
            return;
        }
        ensureCanBeModified();
        this.dangerousGood = dangerousGood;
        markAsModified();
    }

    public void removeDangerousGood() {
        ensureCanBeModified();
        this.dangerousGood = null;
        markAsModified();
    }

    private void ensureCanBeModified() {
        ensureShipmentIsNotDelivered();
        if (Boolean.TRUE.equals(locked)) {
            throw new ShipmentModificationException(
                    "Shipment cannot be changed because it is locked");
        }
        if (ShipmentStatus.SENT.equals(shipmentStatus)
                || ShipmentStatus.DELIVERY.equals(shipmentStatus)
                || ShipmentStatus.RETURN.equals(shipmentStatus)) {
            throw new ShipmentModificationException(
                    "Shipment cannot be changed after it has been sent");
        }
    }

    public void changeShipmentPriority(final ShipmentPriority shipmentPriority) {
        ensureCanBeModified();
        this.shipmentPriority = shipmentPriority;
        markAsModified();
    }

    public void changeShipmentRelatedId(final ShipmentId relatedShipmentId) {
        ensureShipmentIsNotDelivered();
        this.shipmentRelatedId = relatedShipmentId;
        markAsModified();
    }

    public void notifyRelatedShipmentLocked() {
        ensureShipmentIsNotDelivered();
        this.shipmentType = ShipmentType.PARENT;
        this.shipmentRelatedId = null;
        unlockShipment();
        markAsModified();
    }

    public void notifyShipmentRerouted() {
        ensureShipmentIsNotDelivered();
        changeShipmentStatus(ShipmentStatus.REROUTE);
        markAsModified();
    }

    public void notifyShipmentSent() {
        ensureShipmentIsNotDelivered();
        changeShipmentStatus(ShipmentStatus.SENT);
        markAsModified();
    }

    public void notifyShipmentReturned() {
        if (!this.shipmentStatus.equals(ShipmentStatus.DELIVERY)) {
            throw new ShipmentModificationException("Cannot return for not delivered shipment");
        }
        changeShipmentStatus(ShipmentStatus.RETURN);
        markAsModified();
    }

    public void notifyShipmentDelivered() {
        this.shipmentStatus = ShipmentStatus.DELIVERY;
        this.locked = true;
        markAsModified();
    }

    public void notifyShipmentReturnCanceled() {
        if (!this.shipmentStatus.equals(ShipmentStatus.RETURN)) {
            throw new ShipmentModificationException("Cannot undone return for not returned shipment");
        }
        changeShipmentStatus(ShipmentStatus.DELIVERY);
        markAsModified();
    }

    public void changeDestinationDepartment(final DepartmentCode destination) {
        ensureCanBeModified();
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
        ensureCanBeModified();
        this.originCountry = request.issuerCountry();
        this.destinationCountry = request.receiverCountry();
        markAsModified();
    }

    public void changeIssuerCountry(final CountryCode originCountry) {
        ensureCanBeModified();
        this.originCountry = originCountry;
        markAsModified();
    }

    public void changeReceiverCountry(final CountryCode destinationCountry) {
        ensureCanBeModified();
        this.destinationCountry = destinationCountry;
        markAsModified();
    }

    public void changeShipmentTypeWithRelatedId(final ShipmentType shipmentType, final ShipmentId relatedShipmentId) {
        ensureShipmentIsNotDelivered();
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = relatedShipmentId;
        this.locked = true;
        changeShipmentStatus(ShipmentStatus.REDIRECT);
        markAsModified();
    }

    public void lockShipmentWithShipmentType(final ShipmentType shipmentType) {
        ensureShipmentIsNotDelivered();
        this.shipmentType = shipmentType;
        lockShipment();
    }

    public boolean isFullyDelivered() {
        return isLocked() && ShipmentStatus.DELIVERY.equals(this.shipmentStatus);
    }

    public boolean recipientCityMatches(final String city) {
        return this.recipient.getCity().equals(city);
    }

    public Shipment redirectToSender(final ShipmentId shipmentId) {
        ensureShipmentIsNotDelivered();
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
        this.externalShipmentId = ExternalId.randomUUID();
        this.trackingNumber = DomainContext.trackingNumberService().nextTrackingNumber(shipmentId);

        markAsModified();

        return this;
    }

    private void ensureShipmentIsNotDelivered() {
        if (ShipmentStatus.DELIVERY.equals(this.shipmentStatus)) {
            throw new ShipmentModificationException("Delivered shipment cannot be changed");
        }
    }

    public void cancel() {
        final ShipmentWorkflowSettings config = DomainContext
                .shipmentConfigurationServicePort().getCurrentOperatorShipmentConfiguration().workflowSettings();
        if (!draftStatuses().contains(this.shipmentStatus)) {
            throw new ShipmentModificationException("Cannot cancel shipment");
        }
        if (isCancellationWindowExpired(config.cancellationWindowMinutes())) {
            throw new ShipmentModificationException("Shipment cancellation window expired");
        }
        this.locked = true;
        changeShipmentStatus(ShipmentStatus.CANCELED);
        markAsModified();
    }

    private boolean isCancellationWindowExpired(final int cancellationWindowMinutes) {
        if (cancellationWindowMinutes <= 0) {
            return true;
        }

        return LocalDateTime.now().isAfter(this.createdAt.plusMinutes(cancellationWindowMinutes));
    }

    private List<ShipmentStatus> draftStatuses() {
        return List.of(ShipmentStatus.CREATED, ShipmentStatus.ACCEPTED, ShipmentStatus.PREPARED);
    }
}
