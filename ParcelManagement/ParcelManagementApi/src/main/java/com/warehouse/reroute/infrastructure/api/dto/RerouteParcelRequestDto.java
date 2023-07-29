package com.warehouse.reroute.infrastructure.api.dto;

import lombok.*;

@Data
public class RerouteParcelRequestDto {

    ParcelIdDto parcelId;
    ParcelDto parcel;
    TokenDto token;
}
