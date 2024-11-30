package com.warehouse.deliveryreject.domain.port.primary;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    @Override
    public List<DeliveryRejectResponse> registerDeliveryRejection(final List<DeliveryRejectRequest> request) {
        return List.of();
    }
}
