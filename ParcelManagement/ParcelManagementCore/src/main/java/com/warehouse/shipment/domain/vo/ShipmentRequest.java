package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.Shipment;


public final class ShipmentRequest {

	private final Shipment shipment;

	public ShipmentRequest(final Shipment shipment) {
		this.shipment = shipment;
	}

	public Shipment getShipment() {
		return shipment;
	}
}
