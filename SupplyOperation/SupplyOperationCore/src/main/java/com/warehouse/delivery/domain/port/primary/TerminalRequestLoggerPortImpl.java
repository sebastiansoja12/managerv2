package com.warehouse.delivery.domain.port.primary;

import com.warehouse.delivery.domain.port.secondary.DeliveryTrackerLogServicePort;
import com.warehouse.terminal.request.TerminalRequest;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TerminalRequestLoggerPortImpl implements TerminalRequestLoggerPort {

    private final DeliveryTrackerLogServicePort deliveryTrackerLogServicePort;

    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    @Override
    public void logRequest(final TerminalRequest terminalRequest) {
        final String requestAsString = xmlToStringService.convertToString(terminalRequest);
        deliveryTrackerLogServicePort.logRequest(terminalRequest, requestAsString);
    }

    @Override
    public void logDeviceId(final TerminalRequest terminalRequest) {
        deliveryTrackerLogServicePort.logDeviceId(terminalRequest);
    }

    @Override
    public void logVersion(final TerminalRequest terminalRequest) {
        deliveryTrackerLogServicePort.logVersion(terminalRequest);
    }
}
