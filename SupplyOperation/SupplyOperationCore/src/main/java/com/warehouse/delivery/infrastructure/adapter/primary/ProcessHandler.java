package com.warehouse.delivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

public interface ProcessHandler {
    boolean supports(final ProcessType processType);
    DeliveryResponse processRequest(final DeliveryRequest deliveryRequest);
}

