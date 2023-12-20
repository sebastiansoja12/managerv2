package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.ShipmentParcel;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShipmentRequest {

    ShipmentParcel parcel;
}
