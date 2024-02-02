package com.warehouse.deliveryreturn.domain.vo;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryReturnInformation {
	UUID id;
	Long parcelId;
	String depotCode;
	String deliveryStatus;
	String token;
	Boolean locked;
}
