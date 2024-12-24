package com.warehouse.deliverymissed.domain.vo;


import java.util.List;

import com.warehouse.delivery.domain.vo.DeviceInformation;

import lombok.Builder;

@Builder
public class DeliveryMissedResponse {
    private List<DeliveryMissedResponseDetails> deliveryMissedResponseDetails;
    private DeviceInformation deviceInformation;

    public DeliveryMissedResponse(final List<DeliveryMissedResponseDetails> deliveryMissedResponseDetails,
                                  final DeviceInformation deviceInformation) {
        this.deliveryMissedResponseDetails = deliveryMissedResponseDetails;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryMissedResponseDetails> getDeliveryMissedResponseDetails() {
        return deliveryMissedResponseDetails;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }
}
