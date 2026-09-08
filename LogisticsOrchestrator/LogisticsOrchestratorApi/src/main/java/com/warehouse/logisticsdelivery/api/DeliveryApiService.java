package com.warehouse.logisticsdelivery.api;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliveryDto;

public interface DeliveryApiService {

    DeliveryAttemptResultDto recordAttempt(final DeliveryAttemptRequestDto attemptRequest);

    ShipmentDeliveryDto getShipmentDelivery(final ShipmentId shipmentId);
}
