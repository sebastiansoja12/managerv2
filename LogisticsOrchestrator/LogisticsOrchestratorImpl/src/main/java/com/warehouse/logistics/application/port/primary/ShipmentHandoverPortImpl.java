package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;
import com.warehouse.logistics.application.port.secondary.DeliveryHandoverServicePort;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.domain.vo.ProcessCommunication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentHandoverPortImpl implements ShipmentHandoverPort {

    private final DeliveryHandoverServicePort deliveryHandoverServicePort;

    private final DeliveryServicePort deliveryServicePort;

    private final ProcessHubServicePort processHubServicePort;

    public ShipmentHandoverPortImpl(final DeliveryHandoverServicePort deliveryHandoverServicePort,
                                    final DeliveryServicePort deliveryServicePort,
                                    final ProcessHubServicePort processHubServicePort) {
        this.deliveryHandoverServicePort = deliveryHandoverServicePort;
        this.deliveryServicePort = deliveryServicePort;
        this.processHubServicePort = processHubServicePort;
    }

    @Override
    public ShipmentHandoverResult handOver(final ShipmentHandoverCommand command) {
        final ShipmentHandoverResult handover = deliveryHandoverServicePort.handOver(command);
        return new ShipmentHandoverResult(handover.details()
                .stream()
                .map(detail -> completeDelivery(command, detail, handover.recipientSigned()))
                .toList(), handover.recipientSigned());
    }

    private ShipmentHandoverDetail completeDelivery(final ShipmentHandoverCommand command,
                                                    final ShipmentHandoverDetail detail,
                                                    final boolean recipientSigned) {
        final ProcessId processId = command.processId();
        if (!detail.handedOver()) {
            return detail;
        }
        final String request = "CompleteShipmentDeliveryRequest{shipmentId=%s, recipientSigned=%s}"
                .formatted(detail.shipmentId().getValue(), recipientSigned);
        try {
            deliveryServicePort.completeShipmentDelivery(detail.shipmentId(), recipientSigned,
                    command.recipientName(), command.signature());
            registerCommunication(processId, request,
                    "CompleteShipmentDeliveryResponse{shipmentId=%s, completed=true}"
                            .formatted(detail.shipmentId().getValue()), null);
            return detail;
        } catch (final RuntimeException exception) {
            log.warn("Delivery of shipment {} could not be completed: {}",
                    detail.shipmentId().getValue(), exception.getMessage());
            registerCommunication(processId, request, null, exception.getMessage());
            return detail.failed(exception.getMessage());
        }
    }

    private void registerCommunication(final ProcessId processId,
                                       final String request,
                                       final String response,
                                       final String faultDescription) {
        processHubServicePort.registerCommunication(new ProcessCommunication(processId, ProcessType.HANDOVER,
                ServiceType.LOGISTICS_ORCHESTRATOR, request, response, faultDescription));
    }
}
