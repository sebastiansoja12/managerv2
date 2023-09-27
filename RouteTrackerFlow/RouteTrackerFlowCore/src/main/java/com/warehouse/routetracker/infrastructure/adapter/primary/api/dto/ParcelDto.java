package com.warehouse.routetracker.infrastructure.adapter.primary.api.dto;

import lombok.Data;

@Data
public class ParcelDto {

    SenderDto sender;
    RecipientDto recipient;
    ParcelSizeDto parcelSize;
}
