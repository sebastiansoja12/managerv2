package com.warehouse.delivery.domain.port.primary;

import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.port.secondary.DeliveryTrackerLogServicePort;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;
import com.warehouse.xmlconverter.XmlToStringService;
import com.warehouse.xmlconverter.XmlToStringServiceImpl;

public class TerminalRequestLoggerPortImpl implements TerminalRequestLoggerPort {

    private final DeliveryTrackerLogServicePort deliveryTrackerLogServicePort;

    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    public TerminalRequestLoggerPortImpl(final DeliveryTrackerLogServicePort deliveryTrackerLogServicePort) {
        this.deliveryTrackerLogServicePort = deliveryTrackerLogServicePort;
    }

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

    @Override
    public void logDeviceInformation(final TerminalRequest terminalRequest) {
        final DeviceInformation deviceInformation = DeviceInformation.from(terminalRequest.getDevice());
        final Set<ShipmentId> shipmentIds = terminalRequest
                .getDeliveryReturnRequest()
                .getDeliveryReturnDetails()
                .stream()
                .map(DeliveryReturnDetail::getShipmentId)
                .map(ShipmentId::new)
                .collect(Collectors.toSet());
        deliveryTrackerLogServicePort.logDeviceInformation(shipmentIds, deviceInformation, terminalRequest.getProcessType());
    }
}
