package com.warehouse.deliveryreject.dto.request;

import java.util.List;
import java.util.UUID;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ProcessTypeDto;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;

public class DeliveryRejectRequestDto {
    private final List<DeliveryRejectDetailsDto> deliveryRejectDetails;
    private final DeviceInformationDto deviceInformation;
    private final ProcessTypeDto processType;
    private final UUID processId;

    public DeliveryRejectRequestDto(final List<DeliveryRejectDetailsDto> deliveryRejectDetails,
                                    final DeviceInformationDto deviceInformation,
                                    final ProcessTypeDto processType) {
        this(deliveryRejectDetails, deviceInformation, processType, null);
    }

    public DeliveryRejectRequestDto(final List<DeliveryRejectDetailsDto> deliveryRejectDetails,
                                    final DeviceInformationDto deviceInformation,
                                    final ProcessTypeDto processType,
                                    final UUID processId) {
        this.deliveryRejectDetails = deliveryRejectDetails;
        this.deviceInformation = deviceInformation;
        this.processType = processType;
        this.processId = processId;
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

    public UUID getProcessId() {
        return processId;
    }
}
