package com.warehouse.deliverymissed.dto;

import com.warehouse.delivery.dto.*;

import java.util.List;

public class DeliveryMissedRequestDto {
    private DeliveryIdDto deliveryId;
    private List<DeliveryMissedDetailsDto> deliveryMissedDetails;
    private DeviceInformationDto deviceInformation;

    public DeliveryMissedRequestDto() {
    }

    public DeliveryMissedRequestDto(final DeliveryIdDto deliveryId,
                                    final List<DeliveryMissedDetailsDto> deliveryMissedDetails,
                                    final DeviceInformationDto deviceInformation) {
        this.deliveryId = deliveryId;
        this.deliveryMissedDetails = deliveryMissedDetails;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryMissedDetailsDto> getDeliveryMissedDetails() {
        return deliveryMissedDetails;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }

    public DeliveryIdDto getDeliveryId() {
        return deliveryId;
    }
}
