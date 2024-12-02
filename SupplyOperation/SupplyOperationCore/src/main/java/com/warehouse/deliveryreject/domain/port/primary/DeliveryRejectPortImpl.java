package com.warehouse.deliveryreject.domain.port.primary;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    @Override
    public DeliveryRejectResponse registerDeliveryRejection(final DeliveryRejectRequest request) {
        return null;
    }
}
