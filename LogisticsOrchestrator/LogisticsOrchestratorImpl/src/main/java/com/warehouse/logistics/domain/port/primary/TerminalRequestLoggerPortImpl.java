package com.warehouse.logistics.domain.port.primary;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.port.secondary.DeliveryTrackerLogServicePort;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.DeliveryReturnRequestType;
import com.warehouse.terminal.jaxb.TerminalRequest;
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
        final DeviceInformation deviceInformation = JaxbDeviceInformationMapper.map(terminalRequest.getDevice());
        final Set<ShipmentId> shipmentIds = extractShipmentIds(terminalRequest.getDeliveryReturnRequest());
        deliveryTrackerLogServicePort.logDeviceInformation(shipmentIds, deviceInformation, terminalRequest.getProcessType());
    }

    private Set<ShipmentId> extractShipmentIds(final DeliveryReturnRequestType deliveryReturnRequest) {
        if (deliveryReturnRequest == null || deliveryReturnRequest.getDeliveryReturnDetails() == null) {
            return Collections.emptySet();
        }
        return deliveryReturnRequest.getDeliveryReturnDetails()
                .getDeliveryReturnDetail()
                .stream()
                .map(detail -> detail.getShipmentID())
                .map(ShipmentId::new)
                .collect(Collectors.toSet());
    }
}
