package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parcel {

    private Long id;

    private Size parcelSize;

    private String destination;

    private ParcelStatus status;

    private ParcelType parcelType;

    private Long parcelRelatedId;
}
