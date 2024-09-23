package com.warehouse.deliverytoken.domain.vo;


import com.warehouse.commonassets.enumeration.ParcelType;

import lombok.Value;

@Value
public class Parcel {
    Long id;

    Long parcelRelatedId;

    ParcelType parcelType;

    String destination;
}
