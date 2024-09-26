package com.warehouse.deliveryreturn.domain.port.primary;

import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogReturnServicePort;
import com.warehouse.terminal.request.TerminalRequest;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TerminalRequestLoggerPortImpl implements TerminalRequestLoggerPort {

    private final RouteLogReturnServicePort routeLogReturnServicePort;
    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    @Override
    public void logDeliveryMissedTerminalRequest(final TerminalRequest terminalRequest) {
        final String requestAsString = xmlToStringService.convertToString(terminalRequest);
        routeLogReturnServicePort.logRequest(terminalRequest, requestAsString);
    }

    @Override
    public void logTerminalId(final TerminalRequest terminalRequest) {
        routeLogReturnServicePort.logTerminalId(terminalRequest);
    }

    @Override
    public void logVersion(final TerminalRequest terminalRequest) {
        routeLogReturnServicePort.logVersion(terminalRequest);
    }
}
