package com.warehouse.delivery.domain.vo;

import java.util.List;

import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

public class Response {
    private final DeliveryResponse deliveryResponse;
    private final DeviceInformation deviceInformation;
    private final List<DeliveryReturnResponse> deliveryReturnResponses;
    private final List<DeliveryRejectResponse> deliveryRejectResponses;
    private final List<DeliveryMissedResponse> deliveryMissedResponses;

    public Response(final DeliveryResponse deliveryResponse,
                    final DeviceInformation deviceInformation,
                    final List<DeliveryReturnResponse> deliveryReturnResponses,
                    final List<DeliveryRejectResponse> deliveryRejectResponses,
                    final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deliveryResponse = deliveryResponse;
        this.deviceInformation = deviceInformation;
        this.deliveryReturnResponses = deliveryReturnResponses;
        this.deliveryRejectResponses = deliveryRejectResponses;
        this.deliveryMissedResponses = deliveryMissedResponses;
    }

    public DeliveryResponse getDeliveryResponse() {
        return deliveryResponse;
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
}
