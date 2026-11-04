package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.primary.command.ShipmentAcceptCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptResult;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.application.port.secondary.ShipmentAcceptServicePort;
import com.warehouse.logistics.domain.vo.ProcessCommunication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentAcceptPortImpl implements ShipmentAcceptPort {

    private final ShipmentAcceptServicePort shipmentAcceptServicePort;

    private final ProcessHubServicePort processHubServicePort;

    public ShipmentAcceptPortImpl(final ShipmentAcceptServicePort shipmentAcceptServicePort,
                                  final ProcessHubServicePort processHubServicePort) {
        this.shipmentAcceptServicePort = shipmentAcceptServicePort;
        this.processHubServicePort = processHubServicePort;
    }

    @Override
    public ShipmentAcceptResult accept(final ShipmentAcceptCommand command) {
        return new ShipmentAcceptResult(command.trackingNumbers()
                .stream()
                .map(trackingNumber -> accept(command.processId(), trackingNumber))
                .toList());
    }

    private ShipmentAcceptDetail accept(final ProcessId processId, final TrackingNumber trackingNumber) {
        final String request = "AcceptShipmentInDepartmentRequest{trackingNumber=%s}".formatted(trackingNumber.value());
        try {
            final ShipmentId shipmentId = shipmentAcceptServicePort.acceptInDepartment(trackingNumber);
            registerCommunication(processId, request,
                    "AcceptShipmentInDepartmentResponse{shipmentId=%s}".formatted(shipmentId.getValue()), null);
            log.info("Shipment {} accepted in department", shipmentId.getValue());
            return ShipmentAcceptDetail.accepted(trackingNumber, shipmentId);
        } catch (final RuntimeException exception) {
            log.warn("Shipment with tracking number {} could not be accepted in department: {}",
                    trackingNumber.value(), exception.getMessage());
            registerCommunication(processId, request, null, exception.getMessage());
            return ShipmentAcceptDetail.failed(trackingNumber, exception.getMessage());
        }
    }

    private void registerCommunication(final ProcessId processId,
                                       final String request,
                                       final String response,
                                       final String faultDescription) {
        processHubServicePort.registerCommunication(new ProcessCommunication(processId, ProcessType.ACCEPT,
                ServiceType.SHIPMENT_MANAGEMENT, request, response, faultDescription));
    }
}
