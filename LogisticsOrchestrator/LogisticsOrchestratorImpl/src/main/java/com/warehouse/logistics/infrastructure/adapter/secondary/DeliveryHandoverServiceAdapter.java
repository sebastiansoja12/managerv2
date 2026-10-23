package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.deliveryhandover.api.DeliveryHandoverApiService;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverRequestDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResponseDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResultDto;
import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;
import com.warehouse.logistics.application.port.secondary.DeliveryHandoverServicePort;

public class DeliveryHandoverServiceAdapter implements DeliveryHandoverServicePort {

    private final DeliveryHandoverApiService deliveryHandoverApiService;

    public DeliveryHandoverServiceAdapter(final DeliveryHandoverApiService deliveryHandoverApiService) {
        this.deliveryHandoverApiService = deliveryHandoverApiService;
    }

    @Override
    public ShipmentHandoverResult handOver(final ShipmentHandoverCommand command) {
        final DeliveryHandoverResponseDto response = deliveryHandoverApiService.handOver(new DeliveryHandoverRequestDto(
                command.processId(), command.trackingNumbers(), command.recipientName(), command.signature()));
        return new ShipmentHandoverResult(response.results()
                .stream()
                .map(this::map)
                .toList(), response.recipientSigned());
    }

    private ShipmentHandoverDetail map(final DeliveryHandoverResultDto result) {
        return new ShipmentHandoverDetail(result.trackingNumber(), result.shipmentId(), result.handedOver(),
                result.errorMessage());
    }
}
