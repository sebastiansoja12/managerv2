package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.DangerousGood;


public class ShipmentCreateRequest {

	private Sender sender;

	private Recipient recipient;

	private ShipmentSize shipmentSize;
	
	private Money price;
	
	private DangerousGood dangerousGood;

	private Country originCountry;

	private Country destinationCountry;

	public ShipmentCreateRequest() {

	}

	public ShipmentCreateRequest(final DangerousGood dangerousGood,
                                 final Money price,
                                 final Recipient recipient,
                                 final Sender sender,
                                 final ShipmentSize shipmentSize,
								 final Country originCountry,
                                 final Country destinationCountry) {
		this.dangerousGood = dangerousGood;
		this.price = price;
		this.recipient = recipient;
		this.sender = sender;
		this.shipmentSize = shipmentSize;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
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

	public Country getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(final Country originCountry) {
		this.originCountry = originCountry;
	}

	public Country getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(final Country destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public boolean isDangerousGood() {
		return dangerousGood != null;
	}
}
