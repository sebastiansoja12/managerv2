package com.warehouse.deliveryreturn.infrastructure.api.dto;


import java.util.List;

import com.warehouse.delivery.dto.DeviceInformationDto;

public class DeliveryReturnResponseDto {
    private final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponseDetails;
    private final DeviceInformationDto deviceInformation;

    public DeliveryReturnResponseDto(final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponseDetails,
                                     final DeviceInformationDto deviceInformation) {
        this.deliveryReturnResponseDetails = deliveryReturnResponseDetails;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryReturnResponseDetailsDto> getDeliveryReturnResponseDetails() {
        return deliveryReturnResponseDetails;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }
}
