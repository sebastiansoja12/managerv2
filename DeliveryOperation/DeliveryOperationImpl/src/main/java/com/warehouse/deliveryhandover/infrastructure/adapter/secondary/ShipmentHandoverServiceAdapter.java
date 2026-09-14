package com.warehouse.deliveryhandover.infrastructure.adapter.secondary;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliveryhandover.application.port.secondary.ShipmentHandoverServicePort;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;
import com.warehouse.deliveryhandover.domain.vo.RecipientConfirmation;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentHandoverRequestDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentHandoverServiceAdapter implements ShipmentHandoverServicePort {

    private final ShipmentApiService shipmentApiService;

    private final ProcessHubEventPublisher processHubEventPublisher;

    public ShipmentHandoverServiceAdapter(final ShipmentApiService shipmentApiService,
                                          final ProcessHubEventPublisher processHubEventPublisher) {
        this.shipmentApiService = shipmentApiService;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public HandoverResult handOver(final ProcessId processId,
                                   final TrackingNumber trackingNumber,
                                   final RecipientConfirmation recipientConfirmation) {
        final String requestLog = "HandoverShipmentRequest{trackingNumber=%s, signed=%s}"
                .formatted(trackingNumber.value(), recipientConfirmation.signed());
        try {
            final ShipmentDetailsDto shipment = shipmentApiService.handOverShipment(
                    new ShipmentHandoverRequestDto(trackingNumber));
            publishCommunication(processId, requestLog, formatResponse(shipment), null);
            log.info("Shipment {} handed over to recipient", shipment.shipmentId().getValue());
            return HandoverResult.handedOver(trackingNumber, shipment.shipmentId());
        } catch (final RuntimeException exception) {
            log.warn("Shipment with tracking number {} could not be handed over: {}",
                    trackingNumber.value(), exception.getMessage());
            publishCommunication(processId, requestLog, null, exception.getMessage());
            return HandoverResult.failed(trackingNumber, exception.getMessage());
        }
    }

    private void publishCommunication(final ProcessId processId,
                                      final String requestLog,
                                      final String responseLog,
                                      final String faultDescription) {
        processHubEventPublisher.publish(new ProcessCommunicationEvent(
                processId,
                ServiceType.DELIVERY_OPERATION,
                ServiceType.SHIPMENT_MANAGEMENT,
                ProcessType.HANDOVER,
                LocalDateTime.now(),
                requestLog,
                responseLog,
                faultDescription
        ));
    }

    private String formatResponse(final ShipmentDetailsDto shipment) {
        return "HandoverShipmentResponse{shipmentId=%s, shipmentStatus=%s}"
                .formatted(shipment.shipmentId().getValue(), shipment.status());
    }
}
