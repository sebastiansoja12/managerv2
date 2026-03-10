package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessShipmentRejectedFailedEvent extends ProcessShipmentRejectedEvent {

    private final String faultDescription;

    public ProcessShipmentRejectedFailedEvent(final ProcessId processLogId,
                                              final ServiceType serviceType,
                                              final LocalDateTime createdAt,
                                              final String request,
                                              final String response,
                                              final String faultDescription) {
        super(processLogId, serviceType, createdAt, request, response);
        this.faultDescription = faultDescription;
    }

    public String getFaultDescription() {
        return faultDescription;
    }
}
