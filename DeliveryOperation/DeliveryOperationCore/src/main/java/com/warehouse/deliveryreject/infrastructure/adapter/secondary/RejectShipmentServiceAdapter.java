package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper.RejectShipmentServiceMapper;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessShipmentRejectedEvent;
import com.warehouse.process.infrastructure.event.ProcessShipmentRejectedFailedEvent;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RejectShipmentServiceAdapter implements RejectShipmentServicePort {

    private final ShipmentApiService shipmentApiService;
    private final RejectShipmentServiceMapper mapper;
    private final ProcessHubEventPublisher processHubEventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RejectShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                        final RejectShipmentServiceMapper mapper,
                                        final ProcessHubEventPublisher processHubEventPublisher) {
        this.shipmentApiService = shipmentApiService;
        this.mapper = mapper;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public List<ShipmentRejectResponse> notifyShipmentRejection(final List<ShipmentRejectRequest> requests, 
                                                                final ProcessId processId) {
        final ShipmentRejectRequestDto shipmentRejectRequest = mapper.mapRequest(requests);
        try {
            final ShipmentRejectResponseDto rejectResponse = shipmentApiService.rejectShipment(shipmentRejectRequest);
            publishSuccessEvent(processId, shipmentRejectRequest, rejectResponse);
            return mapper.mapResponse(rejectResponse);
        } catch (final RuntimeException e) {
            publishFailureEvent(processId, shipmentRejectRequest, e);
            log.error("Shipment rejection notification failed for {} shipments", requests.size(), e);
            throw e;
        }
    }

	private void publishSuccessEvent(final ProcessId processId, final ShipmentRejectRequestDto request,
			final ShipmentRejectResponseDto response) {
		processHubEventPublisher.publish(new ProcessShipmentRejectedEvent(processId, ServiceType.DELIVERY_OPERATION,
				LocalDateTime.now(), writeAsString(request), writeAsString(response)));
	}

	private void publishFailureEvent(final ProcessId processId, final ShipmentRejectRequestDto request,
			final RuntimeException exception) {
		processHubEventPublisher
				.publish(new ProcessShipmentRejectedFailedEvent(processId, ServiceType.DELIVERY_OPERATION,
						LocalDateTime.now(), writeAsString(request), null, exception.getMessage()));
	}

    private String writeAsString(final Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            log.warn("Cannot serialize shipment rejection process payload", e);
            return String.valueOf(value);
        }
    }
}
