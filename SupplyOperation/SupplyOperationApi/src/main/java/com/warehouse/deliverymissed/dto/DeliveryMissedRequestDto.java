package com.warehouse.deliverymissed.dto;

import com.warehouse.delivery.dto.*;

import java.util.List;

public class DeliveryMissedRequestDto {
    private List<DeliveryMissedDetailsDto> deliveryMissedDetails;
    private DeviceInformationDto deviceInformation;

    public DeliveryMissedRequestDto() {
    }

    public DeliveryMissedRequestDto(final List<DeliveryMissedDetailsDto> deliveryMissedDetails,
                                    final DeviceInformationDto deviceInformation) {
        this.deliveryMissedDetails = deliveryMissedDetails;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryMissedDetailsDto> getDeliveryMissedDetails() {
        return deliveryMissedDetails;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }
}
