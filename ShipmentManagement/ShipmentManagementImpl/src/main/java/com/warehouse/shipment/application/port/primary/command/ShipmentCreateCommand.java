package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

public class ShipmentCreateCommand {

	private Sender sender;

	private Recipient recipient;

	private ShipmentSize shipmentSize;
	
	private Money price;
	
	private DangerousGood dangerousGood;

	private CountryCode issuerCountryCode;

	private CountryCode receiverCountryCode;

	private ShipmentPriority shipmentPriority;

	private PickupMethod pickupMethod;

	private DeliveryMethod deliveryMethod;

	private PickupPointId pickupPointId;

	private PickupPointId deliveryPickupPointId;

	public ShipmentCreateCommand() {

	}

	public ShipmentCreateCommand(final DangerousGood dangerousGood,
								 final Money price,
								 final Recipient recipient,
								 final Sender sender,
								 final ShipmentSize shipmentSize,
								 final CountryCode issuerCountryCode,
								 final CountryCode receiverCountryCode,
								 final ShipmentPriority shipmentPriority) {
		this(dangerousGood, price, recipient, sender, shipmentSize, issuerCountryCode,
				receiverCountryCode, shipmentPriority, PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null);
	}

	public ShipmentCreateCommand(final DangerousGood dangerousGood,
								 final Money price,
								 final Recipient recipient,
								 final Sender sender,
								 final ShipmentSize shipmentSize,
								 final CountryCode issuerCountryCode,
								 final CountryCode receiverCountryCode,
								 final ShipmentPriority shipmentPriority,
								 final PickupMethod pickupMethod,
								 final DeliveryMethod deliveryMethod,
								 final PickupPointId pickupPointId) {
		this(dangerousGood, price, recipient, sender, shipmentSize, issuerCountryCode, receiverCountryCode,
				shipmentPriority, pickupMethod, deliveryMethod, pickupPointId, null);
	}

	public ShipmentCreateCommand(final DangerousGood dangerousGood,
								 final Money price,
								 final Recipient recipient,
								 final Sender sender,
								 final ShipmentSize shipmentSize,
								 final CountryCode issuerCountryCode,
								 final CountryCode receiverCountryCode,
								 final ShipmentPriority shipmentPriority,
								 final PickupMethod pickupMethod,
								 final DeliveryMethod deliveryMethod,
								 final PickupPointId pickupPointId,
								 final PickupPointId deliveryPickupPointId) {
		this.dangerousGood = dangerousGood;
		this.price = price;
		this.recipient = recipient;
		this.sender = sender;
		this.shipmentSize = shipmentSize;
		this.issuerCountryCode = issuerCountryCode;
		this.receiverCountryCode = receiverCountryCode;
		this.shipmentPriority = shipmentPriority;
		this.pickupMethod = pickupMethod;
		this.deliveryMethod = deliveryMethod;
		this.pickupPointId = pickupPointId;
		this.deliveryPickupPointId = deliveryPickupPointId;
	}

	public DangerousGood getDangerousGood() {
		return dangerousGood;
	}

	public void setDangerousGood(final DangerousGood dangerousGood) {
		this.dangerousGood = dangerousGood;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(final Money price) {
		this.price = price;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(final Recipient recipient) {
		this.recipient = recipient;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(final Sender sender) {
		this.sender = sender;
	}

	public ShipmentSize getShipmentSize() {
		return shipmentSize;
	}

	public void setShipmentSize(final ShipmentSize shipmentSize) {
		this.shipmentSize = shipmentSize;
	}

	public CountryCode getIssuerCountryCode() {
		return issuerCountryCode;
	}

	public void setIssuerCountryCode(final CountryCode issuerCountryCode) {
		this.issuerCountryCode = issuerCountryCode;
	}

	public CountryCode getReceiverCountryCode() {
		return receiverCountryCode;
	}

	public void setReceiverCountryCode(final CountryCode receiverCountryCode) {
		this.receiverCountryCode = receiverCountryCode;
	}

	public boolean isDangerousGood() {
		return dangerousGood != null;
	}

	public ShipmentPriority getShipmentPriority() {
		return shipmentPriority;
	}

	public void setShipmentPriority(final ShipmentPriority shipmentPriority) {
		this.shipmentPriority = shipmentPriority;
	}

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
}
