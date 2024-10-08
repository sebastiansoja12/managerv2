package com.warehouse.deliverytoken.domain.vo;


import com.warehouse.commonassets.enumeration.ShipmentType;

import lombok.Value;

@Value
public class Parcel {
    Long id;

    Long parcelRelatedId;

    ShipmentType shipmentType;

    String destination;
}
