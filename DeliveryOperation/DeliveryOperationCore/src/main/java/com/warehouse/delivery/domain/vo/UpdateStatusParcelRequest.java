package com.warehouse.delivery.domain.vo;


import lombok.Value;

@Value
public class UpdateStatusParcelRequest {
    Long parcelId;
    ParcelStatus parcelStatus = ParcelStatus.DELIVERY;
}
