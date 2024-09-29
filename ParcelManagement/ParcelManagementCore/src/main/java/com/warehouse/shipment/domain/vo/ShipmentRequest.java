package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.ShipmentParcel;


public class ShipmentRequest {

	private final ShipmentParcel parcel;

	public ShipmentRequest(final ShipmentParcel parcel) {
		this.parcel = parcel;
	}

	public ShipmentParcel getParcel() {
		return parcel;
	}
}
