package com.warehouse.deliverymissed.domain.port.primary;

import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.XmlToStringService;
import com.warehouse.deliverymissed.domain.service.XmlToStringServiceImpl;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TerminalRequestLoggerPortImpl implements TerminalRequestLoggerPort {

    private final RouteLogMissedServicePort routeLogMissedServicePort;

    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    @Override
    public void logDeliveryMissedTerminalRequest(final TerminalRequest terminalRequest) {
        final String requestAsString = xmlToStringService.convertToString(terminalRequest);
        routeLogMissedServicePort.logRequest(terminalRequest, requestAsString);
    }

    @Override
    public void logTerminalId(final TerminalRequest terminalRequest) {
        routeLogMissedServicePort.logTerminalId(terminalRequest);
    }

    @Override
    public void logVersion(final TerminalRequest terminalRequest) {
        routeLogMissedServicePort.logVersion(terminalRequest);
    }
}
