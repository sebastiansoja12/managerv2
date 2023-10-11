package com.warehouse.redirect.infrastructure.api.dto;



import lombok.Data;

@Data
public class RedirectParcelRequestDto {

    ParcelIdDto parcelId;
    ParcelDto parcel;
    TokenDto token;
}
