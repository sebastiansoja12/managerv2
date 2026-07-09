package com.warehouse.deliveryreturn.infrastructure.api.dto;

import java.util.List;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ProcessTypeDto;

public class DeliveryReturnRequestDto {
    private ProcessTypeDto processType;
    private ProcessIdDto processId;
    private DeviceInformationDto deviceInformation;
    private List<DeliveryReturnDetailsDto> deliveryReturnDetails;

    public DeliveryReturnRequestDto() {
    }

    public DeliveryReturnRequestDto(final ProcessIdDto processId,
                                    final ProcessTypeDto processType,
                                    final DeviceInformationDto deviceInformation,
                                    final List<DeliveryReturnDetailsDto> deliveryReturnDetails) {
        this.processId = processId;
        this.processType = processType;
        this.deviceInformation = deviceInformation;
        this.deliveryReturnDetails = deliveryReturnDetails;
    }

    public ProcessIdDto getProcessId() {
        return processId;
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
