package com.warehouse.redirect.infrastructure.api.dto;


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
