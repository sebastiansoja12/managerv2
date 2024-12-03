package com.warehouse.deliveryreturn.infrastructure.api.dto;


import java.util.List;

import com.warehouse.delivery.dto.DeviceInformationDto;

public class DeliveryReturnResponseDto {
    private final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponses;
    private final DeviceInformationDto deviceInformation;

    public DeliveryReturnResponseDto(final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponses,
                                     final DeviceInformationDto deviceInformation) {
        this.deliveryReturnResponses = deliveryReturnResponses;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryReturnResponseDetailsDto> getDeliveryReturnResponses() {
        return deliveryReturnResponses;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }
}
