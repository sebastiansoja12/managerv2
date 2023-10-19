package com.warehouse.documentation.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Parcel {

    Long id;

    Sender sender;

    Recipient recipient;

    Size parcelSize;

    String destination;

    Status status;

    ParcelType parcelType;

    Long parcelRelatedId;

    double price;
}
