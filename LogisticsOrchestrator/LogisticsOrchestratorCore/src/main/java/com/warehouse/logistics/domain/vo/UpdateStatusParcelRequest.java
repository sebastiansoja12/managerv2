package com.warehouse.logistics.domain.vo;


import lombok.Value;

@Value
public class UpdateStatusParcelRequest {
    Long parcelId;
    ParcelStatus parcelStatus = ParcelStatus.DELIVERY;
}
