package com.warehouse.deliveryreject.domain.port.primary;

import java.util.Collections;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    private final RejectService rejectService;

    public DeliveryRejectPortImpl(final RejectService rejectService) {
        this.rejectService = rejectService;
    }

    @Override
    public DeliveryRejectResponse registerDeliveryRejection(final DeliveryRejectRequest request) {
        log.info("registerDeliveryRejection");
        return new DeliveryRejectResponse(Collections.emptyList(), request.getDeviceInformation());
    }
}
