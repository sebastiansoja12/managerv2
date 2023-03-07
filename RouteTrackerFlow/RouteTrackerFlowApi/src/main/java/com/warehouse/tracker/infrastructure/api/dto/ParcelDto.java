package com.warehouse.tracker.infrastructure.api.dto;

import com.warehouse.tracker.infrastructure.api.enumeration.ParcelTypeDto;
import lombok.Value;

@Value
public class ParcelDto {

    Long id;
    SenderDto sender;
    RecipientDto recipient;
    ParcelTypeDto parcelType;
}
