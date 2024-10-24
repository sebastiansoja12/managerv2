package com.warehouse.zebra.infrastructure.adapter.primary;


import com.warehouse.zebra.infrastructure.adapter.primary.strategy.ProcessStrategy;
import com.warehouse.zebra.infrastructure.adapter.primary.strategy.ProcessStrategyFactory;
import com.warehouse.zebra.infrastructure.api.requestmodel.TerminalRequest;
import com.warehouse.zebra.infrastructure.api.responsemodel.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TerminalEndpoint {

    private static final String NAMESPACE_URI = "http://manager.org/terminal";

    private final ProcessStrategyFactory strategyFactory;

    @Autowired
    public TerminalEndpoint(final ProcessStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TerminalRequest")
    @ResponsePayload
    public TerminalResponse processRequest(@RequestPayload final TerminalRequest request) {

        final TerminalResponse response = new TerminalResponse();

        final ProcessStrategy strategy = strategyFactory.getStrategy(request.getProcessType().name());

        return response;
    }

}
