package com.warehouse.logistics.domain.vo;

import com.warehouse.logistics.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryInformation {
	Long parcelId;
	String depotCode;
	DeliveryStatus deliveryStatus;
}
