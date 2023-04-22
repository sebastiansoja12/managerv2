package com.warehouse.reroute.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParcelResponseDto {
    ParcelIdDto parcelId;
    SenderDto sender;
    RecipientDto recipient;
    ParcelSizeDto parcelSize;
}
