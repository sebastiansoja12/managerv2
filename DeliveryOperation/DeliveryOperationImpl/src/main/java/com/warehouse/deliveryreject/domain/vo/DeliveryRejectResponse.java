package com.warehouse.deliveryreject.domain.vo;

import java.util.List;

import com.warehouse.terminal.DeviceInformation;



public class DeliveryRejectResponse {
    private final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails;
    private final DeviceInformation deviceInformation;

    public DeliveryRejectResponse(final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails,
                                  final DeviceInformation deviceInformation) {
        this.deliveryRejectResponseDetails = deliveryRejectResponseDetails;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryRejectResponseDetails> getDeliveryRejectResponseDetails() {
        return deliveryRejectResponseDetails;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }
}