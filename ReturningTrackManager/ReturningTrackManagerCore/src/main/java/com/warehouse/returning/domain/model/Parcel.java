package com.warehouse.returning.domain.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parcel {

    private Long id;

    private Size parcelSize;

    private String destination;

    private Status status;

    private ParcelType parcelType;

    private Long parcelRelatedId;
}
