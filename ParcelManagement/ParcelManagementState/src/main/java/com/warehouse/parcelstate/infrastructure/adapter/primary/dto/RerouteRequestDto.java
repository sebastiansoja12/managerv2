package com.warehouse.parcelstate.infrastructure.adapter.primary.dto;

import lombok.Data;

@Data
public class RerouteRequestDto {
    TokenDto token;
    ParcelDto parcel;
}
