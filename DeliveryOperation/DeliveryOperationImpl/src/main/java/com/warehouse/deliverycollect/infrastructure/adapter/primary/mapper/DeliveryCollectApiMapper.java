package com.warehouse.deliverycollect.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverycollect.api.dto.DeliveryCollectRequestDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResponseDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResultDto;
import com.warehouse.deliverycollect.application.port.primary.command.DeliveryCollectCommand;
import com.warehouse.deliverycollect.application.port.primary.result.DeliveryCollectResult;
import com.warehouse.deliverycollect.domain.vo.CollectResult;

public class DeliveryCollectApiMapper {

    public DeliveryCollectCommand map(final DeliveryCollectRequestDto deliveryCollectRequest) {
        return new DeliveryCollectCommand(deliveryCollectRequest.processId(), deliveryCollectRequest.trackingNumbers());
    }

    public DeliveryCollectResponseDto map(final DeliveryCollectResult deliveryCollectResult) {
        return new DeliveryCollectResponseDto(deliveryCollectResult.results()
                .stream()
                .map(this::map)
                .toList());
    }

    private DeliveryCollectResultDto map(final CollectResult collectResult) {
        return new DeliveryCollectResultDto(
                collectResult.trackingNumber(),
                collectResult.shipmentId(),
                collectResult.collected(),
                collectResult.errorMessage());
    }
}
