package com.warehouse.delivery.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.vo.DeviceInformation;


public class Request {

    private ProcessType processType;

    private DeviceInformation deviceInformation;

    private DeliveryRejectRequest deliveryRejectRequest;

    private List<DeliveryMissedRequest> deliveryMissedRequests;

	public Request(final ProcessType processType,
                   final DeviceInformation deviceInformation,
                   final DeliveryRejectRequest deliveryRejectRequest,
                   final List<DeliveryMissedRequest> deliveryMissedRequests) {
        this.processType = processType;
        this.deviceInformation = deviceInformation;
        this.deliveryRejectRequest = deliveryRejectRequest;
        this.deliveryMissedRequests = deliveryMissedRequests;
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

    public List<DeliveryMissedRequest> getDeliveryMissedRequests() {
        return deliveryMissedRequests;
    }

    public void setDeliveryMissedRequests(final List<DeliveryMissedRequest> deliveryMissedRequests) {
        this.deliveryMissedRequests = deliveryMissedRequests;
    }
}
