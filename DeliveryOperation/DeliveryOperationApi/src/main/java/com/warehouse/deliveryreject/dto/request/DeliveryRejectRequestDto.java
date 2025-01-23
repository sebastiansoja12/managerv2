package com.warehouse.deliveryreject.dto.request;

import java.util.List;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ProcessTypeDto;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;

public class DeliveryRejectRequestDto {
    private final List<DeliveryRejectDetailsDto> deliveryRejectDetails;
    private final DeviceInformationDto deviceInformation;
    private final ProcessTypeDto processType;

    public DeliveryRejectRequestDto(final List<DeliveryRejectDetailsDto> deliveryRejectDetails,
                                    final DeviceInformationDto deviceInformation,
                                    final ProcessTypeDto processType) {
        this.deliveryRejectDetails = deliveryRejectDetails;
        this.deviceInformation = deviceInformation;
        this.processType = processType;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public List<DeliveryRejectDetailsDto> getDeliveryRejectDetails() {
        return deliveryRejectDetails;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }
}
