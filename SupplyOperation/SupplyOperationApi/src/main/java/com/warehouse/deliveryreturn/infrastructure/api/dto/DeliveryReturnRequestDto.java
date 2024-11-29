package com.warehouse.deliveryreturn.infrastructure.api.dto;

import java.util.List;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ProcessTypeDto;

public class DeliveryReturnRequestDto {
    private ProcessTypeDto processType;
    private DeviceInformationDto deviceInformation;
    private List<DeliveryReturnDetailsDto> deliveryReturnDetails;

    public DeliveryReturnRequestDto() {
    }

    public DeliveryReturnRequestDto(final ProcessTypeDto processType,
                                    final DeviceInformationDto deviceInformation,
                                    final List<DeliveryReturnDetailsDto> deliveryReturnDetails) {
        this.processType = processType;
        this.deviceInformation = deviceInformation;
        this.deliveryReturnDetails = deliveryReturnDetails;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }

    public List<DeliveryReturnDetailsDto> getDeliveryReturnDetails() {
        return deliveryReturnDetails;
    }
}
