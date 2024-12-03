package com.warehouse.deliveryreturn.domain.vo;


import java.util.List;

import com.warehouse.delivery.domain.vo.DeviceInformation;

import lombok.Builder;



@Builder
public class DeliveryReturnResponse {
    private final List<DeliveryReturnResponseDetails> deliveryReturnResponses;
    private final DeviceInformation deviceInformation;

	public List<DeliveryReturnResponseDetails> getDeliveryReturnResponses() {
		return deliveryReturnResponses;
	}

	public DeviceInformation getDeviceInformation() {
		return deviceInformation;
	}
}
