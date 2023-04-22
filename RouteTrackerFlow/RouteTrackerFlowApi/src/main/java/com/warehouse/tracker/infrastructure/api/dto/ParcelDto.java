package com.warehouse.tracker.infrastructure.api.dto;

import com.warehouse.tracker.infrastructure.api.enumeration.ParcelSizeDto;
import lombok.Value;

@Value
public class ParcelDto {

    Long id;
    SenderDto sender;
    RecipientDto recipient;
    ParcelSizeDto parcelSize;
}
