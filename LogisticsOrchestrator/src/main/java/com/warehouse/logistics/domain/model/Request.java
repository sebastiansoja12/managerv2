package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.terminal.DeviceInformation;


public class Request {

    private ProcessType processType;

    private DeviceInformation deviceInformation;

    private DeliveryRejectRequest deliveryRejectRequest;

    private DeliveryMissedRequest deliveryMissedRequest;

    private DeliveryReturnRequest deliveryReturnRequest;

	public Request(final ProcessType processType,
                   final DeviceInformation deviceInformation,
                   final DeliveryRejectRequest deliveryRejectRequest,
                   final DeliveryMissedRequest deliveryMissedRequest,
                   final DeliveryReturnRequest deliveryReturnRequest) {
        this.processType = processType;
        this.deviceInformation = deviceInformation;
        this.deliveryRejectRequest = deliveryRejectRequest;
        this.deliveryMissedRequest = deliveryMissedRequest;
        this.deliveryReturnRequest = deliveryReturnRequest;
    }

    public DeliveryReturnRequest getDeliveryReturnRequest() {
        return deliveryReturnRequest;
    }

    public void setDeliveryReturnRequest(final DeliveryReturnRequest deliveryReturnRequest) {
        this.deliveryReturnRequest = deliveryReturnRequest;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public DeliveryRejectRequest getDeliveryRejectRequest() {
        return deliveryRejectRequest;
    }

    public void setDeliveryRejectRequest(final DeliveryRejectRequest deliveryRejectRequest) {
        this.deliveryRejectRequest = deliveryRejectRequest;
    }

    public DeliveryMissedRequest getDeliveryMissedRequest() {
        return deliveryMissedRequest;
    }

    public void setDeliveryMissedRequest(final DeliveryMissedRequest deliveryMissedRequest) {
        this.deliveryMissedRequest = deliveryMissedRequest;
    }
}
