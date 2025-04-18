package com.warehouse.reroute.infrastructure.adapter.primary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelDto {

    SenderDto sender;
    RecipientDto recipient;
    ParcelSizeDto parcelSize;

    StatusDto status;

    ParcelTypeDto parcelType;
}
