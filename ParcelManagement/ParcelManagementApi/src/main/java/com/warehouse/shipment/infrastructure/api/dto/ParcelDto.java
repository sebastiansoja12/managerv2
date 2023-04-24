package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Data;

@Data
public class ParcelDto {

    ParcelIdDto parcelId;

    SenderDto sender;

    RecipientDto recipient;

    ParcelSizeDto parcelSize;

    StatusDto status;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;

}
