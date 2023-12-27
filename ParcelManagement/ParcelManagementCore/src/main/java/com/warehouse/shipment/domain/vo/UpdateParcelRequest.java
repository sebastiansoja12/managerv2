package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.Parcel;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateParcelRequest {

    Parcel parcel;
}