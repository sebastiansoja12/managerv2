package com.warehouse.deliveryreject.domain.model;

import java.io.Serializable;
import java.util.List;

import com.warehouse.delivery.domain.vo.DeviceInformation;
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

    public void setDeliveryRejectDetails(final List<DeliveryRejectDetails> deliveryRejectDetails) {
        this.deliveryRejectDetails = deliveryRejectDetails;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryRejectDetails> getDeliveryRejectDetails() {
        return deliveryRejectDetails;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }
}
