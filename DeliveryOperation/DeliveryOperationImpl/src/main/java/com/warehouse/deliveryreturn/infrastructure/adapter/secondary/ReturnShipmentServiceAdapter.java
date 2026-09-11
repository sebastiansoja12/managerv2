package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliveryreturn.domain.port.secondary.ReturnShipmentServicePort;
import com.warehouse.deliveryreturn.domain.vo.ShipmentReturnResult;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentReturnResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReturnShipmentServiceAdapter implements ReturnShipmentServicePort {

    private final ShipmentApiService shipmentApiService;
    private final ProcessHubEventPublisher processHubEventPublisher;

    public ReturnShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                        final ProcessHubEventPublisher processHubEventPublisher) {
        this.shipmentApiService = shipmentApiService;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public ShipmentReturnResult returnShipment(final ProcessId processId, final ShipmentId shipmentId) {
        final String requestLog = "ReturnShipmentRequest{shipmentId=%s}".formatted(shipmentId.getValue());
        try {
            final ShipmentReturnResponseDto response = shipmentApiService.returnShipment(shipmentId);
            publishCommunication(processId, requestLog, "ReturnShipmentResponse{newShipmentId=%s, newTrackingNumber=%s}"
                    .formatted(response.newShipmentId() == null ? null : response.newShipmentId().getValue(),
                            response.newTrackingNumber()), null);
            return ShipmentReturnResult.returned(response.newShipmentId(),
                    response.newTrackingNumber() == null ? null : new TrackingNumber(response.newTrackingNumber()));
        } catch (final RuntimeException exception) {
            log.warn("Shipment {} could not be returned: {}", shipmentId.getValue(), exception.getMessage());
            publishCommunication(processId, requestLog, null, exception.getMessage());
            return ShipmentReturnResult.failed(exception.getMessage());
        }
    }

    private void publishCommunication(final ProcessId processId,
                                      final String requestLog,
                                      final String responseLog,
                                      final String faultDescription) {
        if (processId == null || processHubEventPublisher == null) {
            return;
        }
        processHubEventPublisher.publish(new ProcessCommunicationEvent(
                processId,
                ServiceType.DELIVERY_OPERATION,
                ServiceType.SHIPMENT_MANAGEMENT,
                ProcessType.RETURN,
                LocalDateTime.now(),
                requestLog,
                responseLog,
                faultDescription
        ));
    }
}
