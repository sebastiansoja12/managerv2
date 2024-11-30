package com.warehouse.deliverymissed.dto;

import com.warehouse.delivery.dto.*;

import java.util.List;

public class DeliveryMissedRequestDto {
    private List<DeliveryMissedInformationDto> deliveryMissedInformations;
    private DeviceInformationDto deviceInformationDto;

    public DeliveryMissedRequestDto() {
    }

    public DeliveryMissedRequestDto(final List<DeliveryMissedInformationDto> deliveryMissedInformations,
                                    final DeviceInformationDto deviceInformationDto) {
        this.deliveryMissedInformations = deliveryMissedInformations;
        this.deviceInformationDto = deviceInformationDto;
    }

    public List<DeliveryMissedInformationDto> getDeliveryMissedInformations() {
        return deliveryMissedInformations;
    }

    public DeviceInformationDto getDeviceInformationDto() {
        return deviceInformationDto;
    }
}
