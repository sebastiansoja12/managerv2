package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Data;

@Data
public class ParcelDto {

    ParcelIdDto parcelId;

    SenderDto sender;

    RecipientDto recipient;

    ParcelSizeDto parcelSize;

    StatusDto parcelStatus;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;

    String destination;

    String createdAt;

    String updatedAt;

}
