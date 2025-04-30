package com.warehouse.shipment.domain.vo;

import com.google.common.annotations.VisibleForTesting;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.commonassets.model.Money;


public class ShipmentCreateRequest {

	private Sender sender;

	private Recipient recipient;

	private ShipmentSize shipmentSize;
	
	private Money price;
	
	private DangerousGood dangerousGood;

	@VisibleForTesting
	public ShipmentCreateRequest() {

	}

	public ShipmentCreateRequest(final DangerousGood dangerousGood,
								 final Money price,
								 final Recipient recipient,
								 final Sender sender,
								 final ShipmentSize shipmentSize) {
		this.dangerousGood = dangerousGood;
		this.price = price;
		this.recipient = recipient;
		this.sender = sender;
		this.shipmentSize = shipmentSize;
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

	public boolean isDangerousGood() {
		return dangerousGood != null;
	}
}
