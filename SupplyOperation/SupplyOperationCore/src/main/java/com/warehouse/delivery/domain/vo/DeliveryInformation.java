package com.warehouse.delivery.domain.vo;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DeliveryInformation {
	UUID id;
	Long parcelId;
	String depotCode;
	DeliveryStatus deliveryStatus;
	String token;
}
