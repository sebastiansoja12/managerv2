package com.warehouse.routetracker.infrastructure.api.dto;

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
}
