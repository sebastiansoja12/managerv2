package com.warehouse.parcelstate.infrastructure.api.dto;

import lombok.Data;

@Data
public class RerouteRequestDto {
    TokenDto token;
    ParcelDto parcel;
}
