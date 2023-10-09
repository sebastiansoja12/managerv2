package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Data;

@Data
public class ParcelDto {

    SenderDto sender;
    RecipientDto recipient;
    ParcelSizeDto parcelSize;
}
