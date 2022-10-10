package com.warehouse.reroute.infrastructure.api.dto;

import lombok.Data;

@Data
public class ParcelDto {

    SenderDto sender;
    RecipientDto recipient;
    ParcelTypeDto parcelType;
}
