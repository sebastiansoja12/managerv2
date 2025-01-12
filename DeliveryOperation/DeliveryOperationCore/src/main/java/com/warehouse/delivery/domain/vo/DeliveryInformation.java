package com.warehouse.delivery.domain.vo;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryInformation {
	Long parcelId;
	String depotCode;
	DeliveryStatus deliveryStatus;
}
