package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;

public class ShipmentCreateCommand {

	private Party sender;

	private Party recipient;

	private Money price;
	
	private ShipmentPriority shipmentPriority;

	private PackagingType packagingType;

	private ShipmentServiceLevel serviceLevel;

	private PickupMethod pickupMethod;

	private DeliveryMethod deliveryMethod;

	private PickupPointId pickupPointId;

	private PickupPointId deliveryPickupPointId;

	private Dimensions dimensions;

	private Weight weight;

	private String contentDescription;

	private Money declaredValue;

	private CustomerReference customerReference;

	public ShipmentCreateCommand() {

	}

	public ShipmentCreateCommand(final Money price,
								 final Party recipient,
								 final Party sender,
								 final ShipmentPriority shipmentPriority) {
		this(price, recipient, sender, shipmentPriority, PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null);
	}

	public ShipmentCreateCommand(final Money price,
								 final Party recipient,
								 final Party sender,
								 final ShipmentPriority shipmentPriority,
								 final PickupMethod pickupMethod,
								 final DeliveryMethod deliveryMethod,
								 final PickupPointId pickupPointId) {
		this(price, recipient, sender, shipmentPriority, pickupMethod, deliveryMethod, pickupPointId, null);
	}

	public ShipmentCreateCommand(final Money price,
								 final Party recipient,
								 final Party sender,
								 final ShipmentPriority shipmentPriority,
								 final PickupMethod pickupMethod,
								 final DeliveryMethod deliveryMethod,
								 final PickupPointId pickupPointId,
								 final PickupPointId deliveryPickupPointId) {
		this.price = price;
		this.recipient = recipient;
		this.sender = sender;
		this.shipmentPriority = shipmentPriority;
		this.pickupMethod = pickupMethod;
		this.deliveryMethod = deliveryMethod;
		this.pickupPointId = pickupPointId;
		this.deliveryPickupPointId = deliveryPickupPointId;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(final Money price) {
		this.price = price;
	}

	public Party getRecipient() {
		return recipient;
	}

	public void setRecipient(final Party recipient) {
		this.recipient = recipient;
	}

	public Party getSender() {
		return sender;
	}

	public void setSender(final Party sender) {
		this.sender = sender;
	}

	public ShipmentPriority getShipmentPriority() {
		return shipmentPriority;
	}

	public void setShipmentPriority(final ShipmentPriority shipmentPriority) {
		this.shipmentPriority = shipmentPriority;
	}

	public PackagingType getPackagingType() { return packagingType; }

	public void setPackagingType(final PackagingType packagingType) { this.packagingType = packagingType; }

	public ShipmentServiceLevel getServiceLevel() { return serviceLevel; }

	public void setServiceLevel(final ShipmentServiceLevel serviceLevel) { this.serviceLevel = serviceLevel; }

	public PickupMethod getPickupMethod() {
		return pickupMethod == null ? PickupMethod.DEPARTMENT : pickupMethod;
	}

	public void setPickupMethod(final PickupMethod pickupMethod) {
		this.pickupMethod = pickupMethod;
	}

	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod == null ? DeliveryMethod.COURIER : deliveryMethod;
	}

	public void setDeliveryMethod(final DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public PickupPointId getPickupPointId() {
		return pickupPointId;
	}

	public void setPickupPointId(final PickupPointId pickupPointId) {
		this.pickupPointId = pickupPointId;
	}

	public PickupPointId getDeliveryPickupPointId() {
		return deliveryPickupPointId;
	}

	public void setDeliveryPickupPointId(final PickupPointId deliveryPickupPointId) {
		this.deliveryPickupPointId = deliveryPickupPointId;
	}

	public Dimensions getDimensions() {
		return dimensions;
	}

	public void setDimensions(final Dimensions dimensions) {
		this.dimensions = dimensions;
	}

	public Weight getWeight() {
		return weight;
	}

	public void setWeight(final Weight weight) {
		this.weight = weight;
	}

	public String getContentDescription() {
		return contentDescription;
	}

	public void setContentDescription(final String contentDescription) {
		this.contentDescription = contentDescription;
	}

	public Money getDeclaredValue() {
		return declaredValue;
	}

	public void setDeclaredValue(final Money declaredValue) {
		this.declaredValue = declaredValue;
	}

	public CustomerReference getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(final CustomerReference customerReference) {
		this.customerReference = customerReference;
	}
}
