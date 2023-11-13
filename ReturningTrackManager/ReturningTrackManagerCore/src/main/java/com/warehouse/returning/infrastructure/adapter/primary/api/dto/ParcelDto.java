package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Data;

@Data
public class ParcelDto {
    private Long id;

    private SizeDto parcelSize;

    private String destination;

    private StatusDto status;

    private ParcelTypeDto parcelType;

    private Long parcelRelatedId;
}
