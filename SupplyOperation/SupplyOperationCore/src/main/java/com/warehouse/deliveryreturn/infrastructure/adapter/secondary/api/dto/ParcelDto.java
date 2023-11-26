package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ParcelDto {
    ParcelIdDto parcelId;
    StatusDto parcelStatus;
    SenderDto sender;
    RecipientDto recipient;
}
