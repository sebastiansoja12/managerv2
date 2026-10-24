package com.warehouse.deliveryhandover.infrastructure.adapter.primary.mapper;

import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverRequestDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResponseDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResultDto;
import com.warehouse.deliveryhandover.application.port.primary.command.DeliveryHandoverCommand;
import com.warehouse.deliveryhandover.application.port.primary.result.DeliveryHandoverResult;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;

public class DeliveryHandoverApiMapper {

    public DeliveryHandoverCommand map(final DeliveryHandoverRequestDto deliveryHandoverRequest) {
        return new DeliveryHandoverCommand(
                deliveryHandoverRequest.processId(),
                deliveryHandoverRequest.trackingNumbers(),
                deliveryHandoverRequest.recipientName(),
                deliveryHandoverRequest.signature());
    }

    public DeliveryHandoverResponseDto map(final DeliveryHandoverResult deliveryHandoverResult) {
        return new DeliveryHandoverResponseDto(deliveryHandoverResult.results()
                .stream()
                .map(this::map)
                .toList(), deliveryHandoverResult.recipientSigned());
    }

    private DeliveryHandoverResultDto map(final HandoverResult handoverResult) {
        return new DeliveryHandoverResultDto(
                handoverResult.trackingNumber(),
                handoverResult.shipmentId(),
                handoverResult.handedOver(),
                handoverResult.errorMessage());
    }
}
