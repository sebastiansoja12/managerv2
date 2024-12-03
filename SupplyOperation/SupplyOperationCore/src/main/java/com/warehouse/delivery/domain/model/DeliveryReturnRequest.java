package com.warehouse.delivery.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.vo.DeviceInformation;

public class DeliveryReturnRequest {
    private ProcessType processType;
    private List<DeliveryReturnDetails> deliveryReturnDetails;
    private DeviceInformation deviceInformation;

    public DeliveryReturnRequest(final ProcessType processType,
                                 final List<DeliveryReturnDetails> deliveryReturnDetails,
                                 final DeviceInformation deviceInformation) {
        this.processType = processType;
        this.deliveryReturnDetails = deliveryReturnDetails;
        this.deviceInformation = deviceInformation;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public List<DeliveryReturnDetails> getDeliveryReturnDetails() {
        return deliveryReturnDetails;
    }

    public void setDeliveryReturnDetails(final List<DeliveryReturnDetails> deliveryReturnDetails) {
        this.deliveryReturnDetails = deliveryReturnDetails;
    }
}
