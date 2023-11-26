package com.warehouse.zebra.infrastructure.adapter.secondary.api;


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
