package com.warehouse.logistics.domain.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.terminal.DeviceInformation;

public class Response {
    private DeviceInformation deviceInformation;
    private Set<LogisticsResponse> logisticsRespons;
    private DeliveryReturnResponse deliveryReturnResponse;
    private DeliveryRejectResponse deliveryRejectResponse;
    private List<DeliveryMissedResponse> deliveryMissedResponses;

    public Response(final DeviceInformation deviceInformation,
                    final Set<LogisticsResponse> logisticsRespons,
                    final DeliveryReturnResponse deliveryReturnResponse,
                    final DeliveryRejectResponse deliveryRejectResponse,
                    final List<DeliveryMissedResponse> deliveryMissedResponses) {
        this.deviceInformation = deviceInformation;
        this.logisticsRespons = logisticsRespons;
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

    public Set<LogisticsResponse> getDeliveryResponses() {
        return logisticsRespons;
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

    public void updateLogisticsResponse(final Set<LogisticsResponse> logisticsRespons) {
        this.logisticsRespons = logisticsRespons;
        final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails = deliveryReturnResponse.getDeliveryReturnResponseDetails()
                .stream()
                .map(deliveryReturnResponseDetail -> logisticsRespons.stream()
                        .filter(deliveryResponse -> deliveryResponse.getShipmentId().equals(deliveryReturnResponseDetail.getShipmentId()))
                        .findFirst()
                        .map(deliveryResponse -> new DeliveryReturnResponseDetails(
                                deliveryReturnResponseDetail.getProcessId(),
                                deliveryResponse.getDeliveryId(),
                                deliveryReturnResponseDetail.getDepartmentCode(),
                                deliveryReturnResponseDetail.getSupplierCode(),
                                deliveryReturnResponseDetail.getShipmentId(),
                                deliveryReturnResponseDetail.getDeliveryStatus(),
                                deliveryReturnResponseDetail.getReturnToken(),
                                deliveryReturnResponseDetail.getUpdateStatus()
                        ))
                        .orElse(deliveryReturnResponseDetail))
                .collect(Collectors.toList());

        this.deliveryReturnResponse = DeliveryReturnResponse
                .builder()
                .deviceInformation(this.deviceInformation)
                .deliveryReturnResponseDetails(deliveryReturnResponseDetails)
                .build();
    }

}
