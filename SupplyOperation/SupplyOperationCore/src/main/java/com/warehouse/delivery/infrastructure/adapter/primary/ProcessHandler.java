package com.warehouse.delivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;

public interface ProcessHandler {
    boolean supports(final ProcessType processType);
    Response processRequest(final Request deliveryRequest);
}

