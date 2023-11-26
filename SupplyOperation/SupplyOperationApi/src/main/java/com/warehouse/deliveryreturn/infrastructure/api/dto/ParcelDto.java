package com.warehouse.deliveryreturn.infrastructure.api.dto;

import lombok.Data;

@Data
public class ParcelDto {
    private Long id;

    private String parcelType;

    private Long parcelRelatedId;

    private String parcelStatus;
}
