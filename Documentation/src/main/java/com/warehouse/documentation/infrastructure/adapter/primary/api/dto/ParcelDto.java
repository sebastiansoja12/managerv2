package com.warehouse.documentation.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParcelDto {

    ParcelIdDto parcelId;

    SenderDto sender;

    RecipientDto recipient;

    ParcelSizeDto parcelSize;

    StatusDto status;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;

    String destination;

}
