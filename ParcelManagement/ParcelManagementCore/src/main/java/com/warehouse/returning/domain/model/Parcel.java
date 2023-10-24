package com.warehouse.returning.domain.model;


import lombok.Data;

@Data
public class Parcel {

    private Long id;

    private Size parcelSize;

    private String destination;

    private Status status;

    private ParcelType parcelType;

    private Long parcelRelatedId;
}
