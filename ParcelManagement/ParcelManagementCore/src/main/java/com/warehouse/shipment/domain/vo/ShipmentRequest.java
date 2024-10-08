package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.Shipment;


public class ShipmentRequest {

	private final Shipment parcel;

	public ShipmentRequest(final Shipment parcel) {
		this.parcel = parcel;
	}

	public Shipment getParcel() {
		return parcel;
	}
}
