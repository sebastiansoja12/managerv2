package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Parcel {

	Long id;

	Size parcelSize;

	String destination;

	ParcelStatus status;

	ParcelType parcelType;

	Long parcelRelatedId;
}
