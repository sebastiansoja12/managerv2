package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;

public interface ProcessHandler {
    boolean supports(final ProcessType processType);
    Response processRequest(final Request request);
}

