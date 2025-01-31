package com.warehouse.logistics.infrastructure.adapter.primary;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;

@Component
public class ProcessDispatcher {

    private final List<ProcessHandler> handlers;

    public ProcessDispatcher(List<ProcessHandler> handlers) {
        this.handlers = handlers;
    }

    public Response process(final Request request) {
        final ProcessType processType = request.getProcessType();
        return handlers.stream()
                .filter(handler -> handler.supports(processType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported process type: " + processType))
                .processRequest(request);
    }
}
