package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.Parcel;

import lombok.Value;

@Value
public class UpdateParcelResponse {
    Parcel parcel;
}
