package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Shipment {

    private ShipmentId shipmentId;

    private Sender sender;

    private Recipient recipient;

    private ShipmentSize shipmentSize;

    private DepartmentId targetDepartmentId;

    private DepartmentId originDepartmentId;

    private PickupPointId pickupPointId;

    private PickupPointId deliveryPickupPointId;

    private PickupMethod pickupMethod;

    private DeliveryMethod deliveryMethod;

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

    private Shipment(final ShipmentId shipmentId,
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
                    final DepartmentId targetDepartmentId,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final boolean signatureRequired,
                    final ShipmentPriority shipmentPriority,
                    final DangerousGood dangerousGood,
                    final TrackingNumber trackingNumber,
                    final PickupMethod pickupMethod,
                    final DeliveryMethod deliveryMethod,
                    final PickupPointId pickupPointId,
                    final PickupPointId deliveryPickupPointId,
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
        this.targetDepartmentId = targetDepartmentId;
        this.originDepartmentId = originDepartmentId;
        this.signature = signature;
        this.signatureRequired = signatureRequired;
        this.shipmentPriority = shipmentPriority;
        this.dangerousGood = dangerousGood;
        this.trackingNumber = trackingNumber;
        this.pickupMethod = pickupMethod;
        this.deliveryMethod = deliveryMethod;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
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
                    final DepartmentId targetDepartmentId,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status) {
        this(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, locked, targetDepartmentId, null, signature, shipmentPriority,
                trackingNumber, status);
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
                    final DepartmentId targetDepartmentId,
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
        this.targetDepartmentId = targetDepartmentId;
        this.originDepartmentId = originDepartmentId;
        this.signatureRequired = signature != null;
        this.shipmentPriority = shipmentPriority;
        this.trackingNumber = trackingNumber;
        this.externalShipmentId = ExternalId.randomUUID();
        this.pickupMethod = PickupMethod.DEPARTMENT;
        this.deliveryMethod = DeliveryMethod.COURIER;
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
                    final DepartmentId targetDepartmentId,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status,
                    final DangerousGood dangerousGood) {
        this(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, locked, targetDepartmentId, originDepartmentId, signature, shipmentPriority,
                trackingNumber, status, dangerousGood, PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null);
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
                    final DepartmentId targetDepartmentId,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status,
                    final DangerousGood dangerousGood,
                    final PickupMethod pickupMethod,
                    final DeliveryMethod deliveryMethod,
                    final PickupPointId pickupPointId) {
        this(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, locked, targetDepartmentId, originDepartmentId, signature, shipmentPriority,
                trackingNumber, status, dangerousGood, pickupMethod, deliveryMethod, pickupPointId, null);
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
                    final DepartmentId targetDepartmentId,
                    final DepartmentId originDepartmentId,
                    final Signature signature,
                    final ShipmentPriority shipmentPriority,
                    final TrackingNumber trackingNumber,
                    final ShipmentStatus status,
                    final DangerousGood dangerousGood,
                    final PickupMethod pickupMethod,
                    final DeliveryMethod deliveryMethod,
                    final PickupPointId pickupPointId,
                    final PickupPointId deliveryPickupPointId) {
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
        this.targetDepartmentId = targetDepartmentId;
        this.originDepartmentId = originDepartmentId;
        this.signatureRequired = signature != null;
        this.shipmentPriority = shipmentPriority;
        this.trackingNumber = trackingNumber;
        this.externalShipmentId = ExternalId.randomUUID();
        this.dangerousGood = dangerousGood;
        this.pickupMethod = pickupMethod;
        this.deliveryMethod = deliveryMethod;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
    }

    public static Shipment parentShipment(final ShipmentId shipmentId,
                                          final Sender sender,
                                          final Recipient recipient,
                                          final ShipmentSize shipmentSize,
                                          final ShipmentId shipmentRelatedId,
                                          final CountryCode originCountry,
                                          final CountryCode destinationCountry,
                                          final Money price,
                                          final DepartmentId targetDepartmentId,
                                          final DepartmentId originDepartmentId,
                                          final Signature signature,
                                          final ShipmentPriority shipmentPriority,
                                          final TrackingNumber trackingNumber,
                                          final ShipmentStatus status) {
        return new Shipment(shipmentId, sender, recipient, shipmentSize, shipmentRelatedId, originCountry, destinationCountry,
                price, false, targetDepartmentId, originDepartmentId, signature,
                shipmentPriority, trackingNumber, status);
	}

    public static Shipment rehydrate(final ShipmentId shipmentId,
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
                                     final DepartmentId targetDepartmentId,
                                     final DepartmentId originDepartmentId,
                                     final Signature signature,
                                     final boolean signatureRequired,
                                     final ShipmentPriority shipmentPriority,
                                     final DangerousGood dangerousGood,
                                     final TrackingNumber trackingNumber,
                                     final PickupMethod pickupMethod,
                                     final DeliveryMethod deliveryMethod,
                                     final PickupPointId pickupPointId,
                                     final PickupPointId deliveryPickupPointId,
                                     final ExternalId<UUID> externalShipmentId) {
        return new Shipment(shipmentId, sender, recipient, shipmentSize, shipmentStatus, shipmentType,
                shipmentRelatedId, price, createdAt, updatedAt, locked, originCountry, destinationCountry,
                targetDepartmentId, originDepartmentId, signature, signatureRequired,
                shipmentPriority, dangerousGood,
                trackingNumber, pickupMethod, deliveryMethod, pickupPointId, deliveryPickupPointId,
                externalShipmentId);
    }

	public ShipmentSnapshot snapshot() {
		return new ShipmentSnapshot(shipmentId, sender, recipient, shipmentSize, targetDepartmentId, originDepartmentId, shipmentStatus,
				shipmentType, shipmentRelatedId, price, createdAt, updatedAt, locked, dangerousGood, signatureRequired,
				shipmentPriority, originCountry, destinationCountry, signature,
                trackingNumber, pickupMethod, deliveryMethod, pickupPointId, deliveryPickupPointId,
                externalShipmentId);
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

    public DepartmentId getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public DepartmentId getOriginDepartmentId() {
        return originDepartmentId;
    }

    public PickupPointId getPickupPointId() {
        return pickupPointId;
    }

    public PickupPointId getDeliveryPickupPointId() {
        return deliveryPickupPointId;
    }

    public PickupMethod getPickupMethod() {
        return pickupMethod;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
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

    public Money getPrice() {
        return price;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
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
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.DELIVERY;
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

    public void updateDestination(final DepartmentId targetDepartmentId) {
        ensureCanBeModified();
        this.targetDepartmentId = targetDepartmentId;
        markAsModified();
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
                       final DepartmentId targetDepartmentId, final Boolean signatureRequired) {
        ensureCanBeModified();
        this.recipient = recipient;
        this.sender = sender;
        this.shipmentStatus = shipmentStatus;
        this.shipmentPriority = shipmentPriority;
        this.shipmentSize = shipmentSize;
        this.price = price;
        this.dangerousGood = dangerousGood;
        this.targetDepartmentId = targetDepartmentId;
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
        ensureCanBeModified();
        this.shipmentStatus = shipmentStatus;
        markAsModified();
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
        if (ShipmentStatus.DELIVERY.equals(shipmentStatus)
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
        this.shipmentStatus = ShipmentStatus.REROUTE;
        markAsModified();
    }

    public void notifyShipmentSent() {
        ensureShipmentIsNotDelivered();
        this.shipmentStatus = ShipmentStatus.SENT;
        markAsModified();
    }

    public void notifyShipmentReturned() {
        if (!this.shipmentStatus.equals(ShipmentStatus.DELIVERY)) {
            throw new ShipmentModificationException("Cannot return for not delivered shipment");
        }
        this.shipmentStatus = ShipmentStatus.RETURN;
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
        this.shipmentStatus = ShipmentStatus.DELIVERY;
        markAsModified();
    }

    public void changeTargetDepartment(final DepartmentId targetDepartmentId) {
        ensureCanBeModified();
        this.targetDepartmentId = targetDepartmentId;
        markAsModified();
    }

    public void notifyShipmentAccepted() {
        ensureCanBeModified();
        if (!ShipmentStatus.SENT.equals(this.shipmentStatus)) {
            throw new ShipmentModificationException("Shipment cannot be accepted from status " + shipmentStatus);
        }
        this.shipmentStatus = ShipmentStatus.ACCEPTED;
        markAsModified();
    }

    private void unlockShipment() {
        this.locked = false;
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
        this.shipmentStatus = ShipmentStatus.REDIRECT;
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

    public Shipment redirectToSender(final ShipmentId shipmentId, final TrackingNumber trackingNumber) {
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
        this.trackingNumber = trackingNumber;

        markAsModified();

        return this;
    }

    private void ensureShipmentIsNotDelivered() {
        if (ShipmentStatus.DELIVERY.equals(this.shipmentStatus)) {
            throw new ShipmentModificationException("Delivered shipment cannot be changed");
        }
    }

    public void cancel(final ShipmentWorkflowSettings config, final LocalDateTime currentTime) {
        if (!draftStatuses().contains(this.shipmentStatus)) {
            throw new ShipmentModificationException("Cannot cancel shipment");
        }
        if (isCancellationWindowExpired(config.cancellationWindowMinutes(), currentTime)) {
            throw new ShipmentModificationException("Shipment cancellation window expired");
        }
        this.locked = true;
        this.shipmentStatus = ShipmentStatus.CANCELED;
        markAsModified();
    }

    private boolean isCancellationWindowExpired(final int cancellationWindowMinutes, final LocalDateTime currentTime) {
        if (cancellationWindowMinutes <= 0) {
            return true;
        }

        return currentTime.isAfter(this.createdAt.plusMinutes(cancellationWindowMinutes));
    }

    private List<ShipmentStatus> draftStatuses() {
        return List.of(ShipmentStatus.PLANNED, ShipmentStatus.CREATED, ShipmentStatus.PREPARED);
    }

}
