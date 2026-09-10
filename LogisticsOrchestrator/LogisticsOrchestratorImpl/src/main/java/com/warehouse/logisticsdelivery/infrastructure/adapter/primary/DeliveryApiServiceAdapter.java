package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliveryDto;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryApiMapper;

public class DeliveryApiServiceAdapter implements DeliveryApiService {

    private final DeliveryPort deliveryPort;
    private final DeliveryApiMapper deliveryApiMapper;

    public DeliveryApiServiceAdapter(final DeliveryPort deliveryPort,
                                     final DeliveryApiMapper deliveryApiMapper) {
        this.deliveryPort = deliveryPort;
        this.deliveryApiMapper = deliveryApiMapper;
    }

    @Override
    public DeliveryAttemptResultDto recordAttempt(final DeliveryAttemptRequestDto attemptRequest) {
        return this.deliveryApiMapper.map(this.deliveryPort.recordAttempt(this.deliveryApiMapper.map(attemptRequest)));
    }

    @Override
    public ShipmentDeliveryDto getShipmentDelivery(final ShipmentId shipmentId) {
        return this.deliveryApiMapper.mapShipmentDelivery(this.deliveryPort.findDeliveryByShipmentId(shipmentId));
    }
}
