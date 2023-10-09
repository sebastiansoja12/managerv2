package com.warehouse.delivery;

import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;
import com.warehouse.supplier.dto.DeliveryResponseDto;

public interface DeliveryService {

    DeliveryResponseDto deliver(DeliveryInformationDto supplyInformation);
}
