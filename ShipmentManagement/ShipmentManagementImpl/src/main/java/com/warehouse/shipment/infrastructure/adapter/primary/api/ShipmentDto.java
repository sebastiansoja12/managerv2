package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;

import java.time.LocalDateTime;

public class ShipmentDto {
    
    private final ShipmentIdDto shipmentId;

    private final PersonApi sender;

    private final PersonApi recipient;

    private final DepartmentCodeDto destination;

    private final DepartmentId originDepartmentId;

    private final PickupPointId pickupPointId;

    private PickupPointId deliveryPickupPointId;

    private final PickupMethodDto pickupMethod;

    private final DeliveryMethodDto deliveryMethod;

    private final ShipmentStatusDto shipmentStatus;
    
    private final ShipmentIdDto shipmentRelatedId;

    private final ShipmentPriorityDto shipmentPriority;

    private final PackagingTypeDto packagingType;

    private final ShipmentServiceLevelDto serviceLevel;

    private final TrackingNumberDto trackingNumber;

    private final MoneyApi price;

    private final Boolean locked;

    private final SignatureDto signature;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final DimensionsApi dimensions;

    private final WeightApi weight;

    private final String customerReference;

    private final String contentDescription;

    private final MoneyApi declaredValue;

	public ShipmentDto(final ShipmentIdDto shipmentId, final PersonApi sender, final PersonApi recipient,
                       final DepartmentCodeDto destination,
                       final DepartmentId originDepartmentId, final PickupPointId pickupPointId,
                       final PickupMethodDto pickupMethod, final DeliveryMethodDto deliveryMethod,
                       final ShipmentStatusDto shipmentStatus,
                       final ShipmentIdDto shipmentRelatedId, final ShipmentPriorityDto shipmentPriority,
                       final TrackingNumberDto trackingNumber,
                       final MoneyApi price, final Boolean locked,
                       final SignatureDto signature,
                       final LocalDateTime createdAt, final LocalDateTime updatedAt,
                       final DimensionsApi dimensions, final WeightApi weight,
                       final String customerReference, final String contentDescription,
                       final MoneyApi declaredValue, final PackagingTypeDto packagingType,
                       final ShipmentServiceLevelDto serviceLevel) {
        this.shipmentId = shipmentId;
        this.sender = sender;
		this.recipient = recipient;
        this.destination = destination;
        this.originDepartmentId = originDepartmentId;
        this.pickupPointId = pickupPointId;
        this.pickupMethod = pickupMethod;
        this.deliveryMethod = deliveryMethod;
		this.shipmentStatus = shipmentStatus;
		this.shipmentRelatedId = shipmentRelatedId;
        this.shipmentPriority = shipmentPriority;
        this.packagingType = packagingType;
        this.serviceLevel = serviceLevel;
        this.trackingNumber = trackingNumber;
        this.price = price;
        this.locked = locked;
        this.signature = signature;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dimensions = dimensions;
        this.weight = weight;
        this.customerReference = customerReference;
        this.contentDescription = contentDescription;
        this.declaredValue = declaredValue;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public PersonApi getSender() {
        return sender;
    }

    public PersonApi getRecipient() {
        return recipient;
    }

    public DepartmentCodeDto getDestination() {
        return destination;
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

    public void setDeliveryPickupPointId(final PickupPointId deliveryPickupPointId) {
        this.deliveryPickupPointId = deliveryPickupPointId;
    }

    public PickupMethodDto getPickupMethod() {
        return pickupMethod;
    }

    public DeliveryMethodDto getDeliveryMethod() {
        return deliveryMethod;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public Boolean getLocked() {
        return locked;
    }

    public ShipmentTypeDto getShipmentType() {
        return shipmentRelatedId != null && shipmentRelatedId.getValue() != null ? ShipmentTypeDto.CHILD : ShipmentTypeDto.PARENT;
    }

    public ShipmentIdDto getShipmentRelatedId() {
        return shipmentRelatedId;
    }

    public MoneyApi getPrice() {
        return price;
    }

    public ShipmentPriorityDto getShipmentPriority() {
        return shipmentPriority;
    }

    public PackagingTypeDto getPackagingType() { return packagingType; }

    public ShipmentServiceLevelDto getServiceLevel() { return serviceLevel; }

    public SignatureDto getSignature() {
        return signature;
    }

    public TrackingNumberDto getTrackingNumber() {
        return trackingNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public DimensionsApi getDimensions() {
        return dimensions;
    }

    public WeightApi getWeight() {
        return weight;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public MoneyApi getDeclaredValue() {
        return declaredValue;
    }
}
