package com.warehouse.deliveryhandover.infrastructure.adapter.primary;

import com.warehouse.deliveryhandover.api.DeliveryHandoverApiService;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverRequestDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResponseDto;
import com.warehouse.deliveryhandover.application.port.primary.DeliveryHandoverPort;
import com.warehouse.deliveryhandover.application.port.primary.result.DeliveryHandoverResult;
import com.warehouse.deliveryhandover.infrastructure.adapter.primary.mapper.DeliveryHandoverApiMapper;

public class DeliveryHandoverApiServiceAdapter implements DeliveryHandoverApiService {

    private final DeliveryHandoverPort deliveryHandoverPort;

    private final DeliveryHandoverApiMapper deliveryHandoverApiMapper;

    public DeliveryHandoverApiServiceAdapter(final DeliveryHandoverPort deliveryHandoverPort,
                                             final DeliveryHandoverApiMapper deliveryHandoverApiMapper) {
        this.deliveryHandoverPort = deliveryHandoverPort;
        this.deliveryHandoverApiMapper = deliveryHandoverApiMapper;
    }

    @Override
    public DeliveryHandoverResponseDto handOver(final DeliveryHandoverRequestDto deliveryHandoverRequest) {
        final DeliveryHandoverResult result = deliveryHandoverPort.handOver(
                deliveryHandoverApiMapper.map(deliveryHandoverRequest));
        return deliveryHandoverApiMapper.map(result);
    }
}
