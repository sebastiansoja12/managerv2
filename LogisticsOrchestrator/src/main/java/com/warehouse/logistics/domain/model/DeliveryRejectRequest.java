package com.warehouse.logistics.domain.model;

import java.io.Serializable;
import java.util.List;

import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.enumeration.ExecutionSourceType;
import com.warehouse.terminal.information.ExecutionSourceResolver;

public class DeliveryRejectRequest implements Serializable, ExecutionSourceResolver {
    private List<DeliveryRejectDetails> deliveryRejectDetails;
    private DeviceInformation deviceInformation;

    public DeliveryRejectRequest(final List<DeliveryRejectDetails> deliveryRejectDetails,
                                 final DeviceInformation deviceInformation) {
        this.deliveryRejectDetails = deliveryRejectDetails;
        this.deviceInformation = deviceInformation;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryRejectDetails> getDeliveryRejectDetails() {
        return deliveryRejectDetails;
    }

    public void setDeliveryRejectDetails(final List<DeliveryRejectDetails> deliveryRejectDetails) {
        this.deliveryRejectDetails = deliveryRejectDetails;
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }
}
