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
    private List<DeliveryReturnResponse> deliveryReturnResponses;
    private List<DeliveryRejectResponse> deliveryRejectResponses;
    private List<DeliveryMissedResponse> deliveryMissedResponses;

    public Response(final Set<DeliveryResponse> deliveryResponses,
                    final DeviceInformation deviceInformation,
                    final List<DeliveryReturnResponse> deliveryReturnResponses,
                    final List<DeliveryRejectResponse> deliveryRejectResponses,
                    final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deliveryResponses = deliveryResponses;
        this.deviceInformation = deviceInformation;
        this.deliveryReturnResponses = deliveryReturnResponses;
        this.deliveryRejectResponses = deliveryRejectResponses;
        this.deliveryMissedResponses = deliveryMissedResponses;
    }

    public Set<DeliveryResponse> getDeliveryResponses() {
        return deliveryResponses;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public List<DeliveryReturnResponse> getDeliveryReturnResponses() {
        return deliveryReturnResponses;
    }

    public List<DeliveryRejectResponse> getDeliveryRejectResponses() {
        return deliveryRejectResponses;
    }

    public List<DeliveryMissedResponse> getDeliveryMissedResponses() {
        return deliveryMissedResponses;
    }

    public void updateDeliveryResponse(final Set<DeliveryResponse> deliveryResponse) {
        this.deliveryResponses = deliveryResponse;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public void setDeliveryReturnResponses(final List<DeliveryReturnResponse> deliveryReturnResponses) {
        this.deliveryReturnResponses = deliveryReturnResponses;
    }

    public void setDeliveryRejectResponses(final List<DeliveryRejectResponse> deliveryRejectResponses) {
        this.deliveryRejectResponses = deliveryRejectResponses;
    }

    public void setDeliveryMissedResponses(final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deliveryMissedResponses = deliveryMissedResponses;
    }
}
