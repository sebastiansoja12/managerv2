package com.warehouse.deliverycollect.infrastructure.adapter.secondary;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverycollect.application.port.secondary.ShipmentCollectServicePort;
import com.warehouse.deliverycollect.domain.vo.CollectResult;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentCollectServiceAdapter implements ShipmentCollectServicePort {

    private final ShipmentApiService shipmentApiService;

    private final ProcessHubEventPublisher processHubEventPublisher;

    public ShipmentCollectServiceAdapter(final ShipmentApiService shipmentApiService,
                                         final ProcessHubEventPublisher processHubEventPublisher) {
        this.shipmentApiService = shipmentApiService;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public CollectResult collect(final ProcessId processId, final TrackingNumber trackingNumber) {
        final String requestLog = "CollectShipmentRequest{trackingNumber=%s}".formatted(trackingNumber.value());
        try {
            final ShipmentDetailsDto shipment = shipmentApiService.collectShipment(trackingNumber);
            publishCommunication(processId, requestLog, formatResponse(shipment), null);
            log.info("Shipment {} collected from client", shipment.shipmentId().getValue());
            return CollectResult.collected(trackingNumber, shipment.shipmentId());
        } catch (final RuntimeException exception) {
            log.warn("Shipment with tracking number {} could not be collected: {}",
                    trackingNumber.value(), exception.getMessage());
            publishCommunication(processId, requestLog, null, exception.getMessage());
            return CollectResult.failed(trackingNumber, exception.getMessage());
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
                ProcessType.COLLECT,
                LocalDateTime.now(),
                requestLog,
                responseLog,
                faultDescription
        ));
    }

    private String formatResponse(final ShipmentDetailsDto shipment) {
        return "CollectShipmentResponse{shipmentId=%s, shipmentStatus=%s}"
                .formatted(shipment.shipmentId().getValue(), shipment.status());
    }
}
