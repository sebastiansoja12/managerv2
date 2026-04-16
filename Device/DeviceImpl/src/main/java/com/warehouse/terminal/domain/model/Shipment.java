package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;

public class Shipment {

	private ShipmentId shipmentId;

	private ShipmentSize shipmentSize;

	private String destination;

	private ShipmentStatus shipmentStatus;

	private ShipmentType shipmentType;

	private ShipmentId shipmentRelatedId;

	public Shipment(final ShipmentId shipmentId,
					final ShipmentSize shipmentSize,
					final String destination,
					final ShipmentStatus shipmentStatus,
					final ShipmentType shipmentType,
					final ShipmentId shipmentRelatedId) {
		this.shipmentId = shipmentId;
		this.shipmentSize = shipmentSize;
		this.destination = destination;
		this.shipmentStatus = shipmentStatus;
		this.shipmentType = shipmentType;
		this.shipmentRelatedId = shipmentRelatedId;
	}

	public ShipmentId getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(final ShipmentId shipmentId) {
		this.shipmentId = shipmentId;
	}

	public ShipmentSize getShipmentSize() {
		return shipmentSize;
	}

	public void setShipmentSize(final ShipmentSize shipmentSize) {
		this.shipmentSize = shipmentSize;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(final String destination) {
		this.destination = destination;
	}

	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(final ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	public ShipmentType getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(final ShipmentType shipmentType) {
		this.shipmentType = shipmentType;
	}

	public ShipmentId getShipmentRelatedId() {
		return shipmentRelatedId;
	}

	public void setShipmentRelatedId(final ShipmentId shipmentRelatedId) {
		this.shipmentRelatedId = shipmentRelatedId;
	}
}
