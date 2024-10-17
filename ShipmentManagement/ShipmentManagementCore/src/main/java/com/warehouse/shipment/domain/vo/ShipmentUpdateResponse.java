package com.warehouse.shipment.domain.vo;

public class ShipmentUpdateResponse {
    private final Parcel parcel;

    public ShipmentUpdateResponse(final Parcel parcel) {
        this.parcel = parcel;
    }
    public Parcel getParcel() {
        return parcel;
    }
}
