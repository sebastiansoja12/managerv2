package com.warehouse.shipment.infrastructure.api.dto;

import java.util.Objects;

public class ShipmentRequestDto {

    private final ShipmentParcelDto parcel;

    public ShipmentRequestDto(final ShipmentParcelDto parcel) {
        this.parcel = parcel;
    }

    public ShipmentParcelDto getParcel() {
        return parcel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShipmentRequestDto that = (ShipmentRequestDto) o;
        return Objects.equals(parcel, that.parcel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parcel);
    }
}
