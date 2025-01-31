package com.warehouse.deliveryreturn.domain.vo;


import java.util.List;

import com.warehouse.terminal.DeviceInformation;

import lombok.Builder;



@Builder
public class DeliveryReturnResponse {
    private final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails;
    private final DeviceInformation deviceInformation;

	public List<DeliveryReturnResponseDetails> getDeliveryReturnResponseDetails() {
		return deliveryReturnResponseDetails;
	}

	public DeviceInformation getDeviceInformation() {
		return deviceInformation;
	}
}
