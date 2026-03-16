package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentStatusControlServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusShipmentRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusShipmentRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;
import com.warehouse.tools.shipment.ShipmentProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentStatusControlServiceAdapter extends RestGatewaySupport
		implements ShipmentStatusControlServicePort {

    private final RestClient restClient;

    private final ShipmentProperties shipmentProperties;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ShipmentStatusControlServiceAdapter(final ShipmentProperties shipmentProperties,
                                               final ProcessHubEventPublisher processHubEventPublisher) {
        this.restClient = RestClient.builder().baseUrl(shipmentProperties.getUrl()).build();
        this.shipmentProperties = shipmentProperties;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public UpdateStatus updateStatus(final UpdateStatusShipmentRequest updateStatusShipmentRequest) {
        final UpdateStatusShipmentRequestDto request = updateStatusParcelRequestMapper.map(updateStatusShipmentRequest);
        final String requestLog = formatRequest(request);
        try {
            final UpdateStatus updateStatus = restClient
                    .put()
                    .uri("/v2/api/shipments/status")
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange((req, res) -> {
                        final HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(res.getStatusCode().value());
                        if (httpStatusCode.is2xxSuccessful()) {
                            return UpdateStatus.OK;
                        }
                        if (httpStatusCode.is5xxServerError()) {
                            return UpdateStatus.ERROR;
                        } else {
                            return UpdateStatus.NOT_OK;
                        }
                    });
            publishCommunication(updateStatusShipmentRequest, requestLog, updateStatus.name(), null);
            return updateStatus;
        } catch (final RuntimeException ex) {
            log.error("Error while updating shipment {} status to RETURN",
                    updateStatusShipmentRequest.getShipmentId().getValue(), ex);
            publishCommunication(updateStatusShipmentRequest, requestLog, null, ex.getMessage());
            return UpdateStatus.ERROR;
        }
    }

    private void publishCommunication(final UpdateStatusShipmentRequest request,
                                      final String requestLog,
                                      final String responseLog,
                                      final String faultDescription) {
        if (request.getProcessId() == null || processHubEventPublisher == null) {
            return;
        }
        processHubEventPublisher.publish(new ProcessCommunicationEvent(
                request.getProcessId(),
                ServiceType.DELIVERY_OPERATION,
                ServiceType.SHIPMENT_MANAGEMENT,
                ProcessType.RETURN,
                LocalDateTime.now(),
                requestLog,
                responseLog,
                faultDescription
        ));
    }

    private String formatRequest(final UpdateStatusShipmentRequestDto request) {
        return "UpdateShipmentStatusRequest{shipmentId=%s, shipmentStatus=%s}"
                .formatted(
                        request.getShipmentId() != null ? request.getShipmentId().getValue() : null,
                        request.getShipmentStatus());
    }
}
