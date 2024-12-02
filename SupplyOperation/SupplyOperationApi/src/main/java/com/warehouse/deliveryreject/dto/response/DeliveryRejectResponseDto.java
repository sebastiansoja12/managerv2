package com.warehouse.deliveryreject.dto.response;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.deliveryreject.dto.DeliveryRejectResponseDetailsDto;

import java.util.List;

public record DeliveryRejectResponseDto(List<DeliveryRejectResponseDetailsDto> deliveryRejectResponseDetails,
                                        DeviceInformationDto deviceInformation) {
}
