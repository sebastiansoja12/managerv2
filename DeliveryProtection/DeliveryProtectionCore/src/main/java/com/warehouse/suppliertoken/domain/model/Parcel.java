package com.warehouse.suppliertoken.domain.model;

import com.warehouse.suppliertoken.domain.enumeration.ParcelType;
import lombok.Value;

@Value
public class Parcel {
    Long id;

    Long parcelRelatedId;

    ParcelType parcelType;

    String destination;
}
