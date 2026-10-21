package com.warehouse.deliverycollect.infrastructure.adapter.primary;

import com.warehouse.deliverycollect.api.DeliveryCollectApiService;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectRequestDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResponseDto;
import com.warehouse.deliverycollect.application.port.primary.DeliveryCollectPort;
import com.warehouse.deliverycollect.application.port.primary.result.DeliveryCollectResult;
import com.warehouse.deliverycollect.infrastructure.adapter.primary.mapper.DeliveryCollectApiMapper;

public class DeliveryCollectApiServiceAdapter implements DeliveryCollectApiService {

    private final DeliveryCollectPort deliveryCollectPort;

    private final DeliveryCollectApiMapper deliveryCollectApiMapper;

    public DeliveryCollectApiServiceAdapter(final DeliveryCollectPort deliveryCollectPort,
                                            final DeliveryCollectApiMapper deliveryCollectApiMapper) {
        this.deliveryCollectPort = deliveryCollectPort;
        this.deliveryCollectApiMapper = deliveryCollectApiMapper;
    }

    @Override
    public DeliveryCollectResponseDto collect(final DeliveryCollectRequestDto deliveryCollectRequest) {
        final DeliveryCollectResult result = deliveryCollectPort.collect(deliveryCollectApiMapper.map(deliveryCollectRequest));
        return deliveryCollectApiMapper.map(result);
    }
}
