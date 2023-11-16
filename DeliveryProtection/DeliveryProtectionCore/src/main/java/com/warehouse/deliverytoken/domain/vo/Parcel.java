package com.warehouse.deliverytoken.domain.vo;

import com.warehouse.deliverytoken.domain.enumeration.ParcelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Parcel {
    Long id;

    Long parcelRelatedId;

    ParcelType parcelType;

    String destination;
}
