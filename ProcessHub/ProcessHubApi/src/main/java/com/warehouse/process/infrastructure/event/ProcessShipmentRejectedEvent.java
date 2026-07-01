package com.warehouse.process.infrastructure.event;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

import java.time.LocalDateTime;

public class ProcessShipmentRejectedEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final String request;
    private final String response;

    public ProcessShipmentRejectedEvent(final ProcessId processLogId, final ServiceType serviceType, final LocalDateTime createdAt, final String request, final String response) {
        super(processLogId, serviceType, createdAt);
        this.request = request;
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }
}
