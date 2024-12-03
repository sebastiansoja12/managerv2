package com.warehouse.delivery.domain.model;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

public class Response {
    private DeviceInformation deviceInformation;
    private Set<DeliveryResponse> deliveryResponses;
    private DeliveryReturnResponse deliveryReturnResponse;
    private DeliveryRejectResponse deliveryRejectResponse;
    private List<DeliveryMissedResponse> deliveryMissedResponses;

    public Response(final DeviceInformation deviceInformation,
                    final Set<DeliveryResponse> deliveryResponses,
                    final DeliveryReturnResponse deliveryReturnResponse,
                    final DeliveryRejectResponse deliveryRejectResponse,
                    final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deviceInformation = deviceInformation;
        this.deliveryResponses = deliveryResponses;
        this.deliveryReturnResponse = deliveryReturnResponse;
        this.deliveryRejectResponse = deliveryRejectResponse;
        this.deliveryMissedResponses = deliveryMissedResponses;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public void setDeliveryReturnResponse(final DeliveryReturnResponse deliveryReturnResponse) {
        this.deliveryReturnResponse = deliveryReturnResponse;
    }

    public void setDeliveryRejectResponse(final DeliveryRejectResponse deliveryRejectResponse) {
        this.deliveryRejectResponse = deliveryRejectResponse;
    }

    public void setDeliveryMissedResponses(final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deliveryMissedResponses = deliveryMissedResponses;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public Set<DeliveryResponse> getDeliveryResponses() {
        return deliveryResponses;
    }

    public DeliveryReturnResponse getDeliveryReturnResponse() {
        return deliveryReturnResponse;
    }

    public DeliveryRejectResponse getDeliveryRejectResponse() {
        return deliveryRejectResponse;
    }

    public List<DeliveryMissedResponse> getDeliveryMissedResponses() {
        return deliveryMissedResponses;
    }

    public void updateDeliveryResponse(final Set<DeliveryResponse> deliveryResponses) {
        this.deliveryResponses = deliveryResponses;
    }
}
