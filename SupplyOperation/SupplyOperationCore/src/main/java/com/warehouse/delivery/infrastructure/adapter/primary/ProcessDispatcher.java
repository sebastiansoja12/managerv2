package com.warehouse.delivery.infrastructure.adapter.primary;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

@Component
public class ProcessDispatcher {

    private final List<ProcessHandler> handlers;

    public ProcessDispatcher(List<ProcessHandler> handlers) {
        this.handlers = handlers;
    }

    public DeliveryResponse process(final DeliveryRequest deliveryRequest) {
        final ProcessType processType = deliveryRequest.getProcessType();
        return handlers.stream()
                .filter(handler -> handler.supports(processType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported process type: " + processType))
                .processRequest(deliveryRequest);
    }
}
