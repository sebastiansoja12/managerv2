package com.warehouse.shipment.infrastructure.api.dto;


import lombok.Data;

@Data
public class UpdateParcelRequestDto {
    ParcelDto parcel;
    TokenDto token;
}
