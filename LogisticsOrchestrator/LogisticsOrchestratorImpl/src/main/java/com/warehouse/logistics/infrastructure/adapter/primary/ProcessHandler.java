package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.logistics.infrastructure.adapter.primary.process.Request;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Response;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;

public interface ProcessHandler {
    boolean supports(final ProcessType processType);
    Response processRequest(final ProcessId processId, final Request request);
}
