package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.deliverycollect.api.DeliveryCollectApiService;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectRequestDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResponseDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResultDto;
import com.warehouse.logistics.domain.port.secondary.DeliveryCollectServicePort;
import com.warehouse.logistics.domain.vo.ShipmentCollectCommand;
import com.warehouse.logistics.domain.vo.ShipmentCollectDetail;
import com.warehouse.logistics.domain.vo.ShipmentCollectResult;

public class DeliveryCollectServiceAdapter implements DeliveryCollectServicePort {

    private final DeliveryCollectApiService deliveryCollectApiService;

    public DeliveryCollectServiceAdapter(final DeliveryCollectApiService deliveryCollectApiService) {
        this.deliveryCollectApiService = deliveryCollectApiService;
    }

    @Override
    public ShipmentCollectResult collect(final ShipmentCollectCommand command) {
        final DeliveryCollectResponseDto response = deliveryCollectApiService.collect(
                new DeliveryCollectRequestDto(command.processId(), command.trackingNumbers()));
        return new ShipmentCollectResult(response.results()
                .stream()
                .map(this::map)
                .toList());
    }

    private ShipmentCollectDetail map(final DeliveryCollectResultDto result) {
        return new ShipmentCollectDetail(result.trackingNumber(), result.shipmentId(), result.collected(),
                result.errorMessage());
    }
}
