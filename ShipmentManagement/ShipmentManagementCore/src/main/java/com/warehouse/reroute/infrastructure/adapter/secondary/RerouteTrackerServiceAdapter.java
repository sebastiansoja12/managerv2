package com.warehouse.reroute.infrastructure.adapter.secondary;


import java.util.function.Supplier;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.port.secondary.RerouteTrackerServicePort;
import com.warehouse.reroute.domain.vo.SoftwareConfiguration;
import com.warehouse.reroute.infrastructure.adapter.secondary.api.RerouteProcessDto;
import com.warehouse.reroute.infrastructure.adapter.secondary.api.RerouteRequestDto;
import com.warehouse.reroute.infrastructure.adapter.secondary.api.ShipmentIdDto;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RerouteTrackerServiceAdapter implements RerouteTrackerServicePort {

    private final Retry retry;

	public RerouteTrackerServiceAdapter(final RetryConfig retryConfig) {
        this.retry = Retry.of("rerouteTrackerServiceAdapter", retryConfig);
    }
    
	private ResponseEntity<RerouteProcessDto> sendRerouteRequest(final RestClient restClient,
			final ShipmentId shipmentId, final String value) {
        log.info("Trying to send reroute process request for shipment {}", shipmentId.getValue());
        final ShipmentIdDto shipmentIdDto = new ShipmentIdDto(shipmentId.getValue());
        final RerouteRequestDto rerouteRequest = new RerouteRequestDto(shipmentIdDto, "REROUTE");
        return restClient
                .post()
                .uri(value)
                .body(rerouteRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(RerouteProcessDto.class);
    }

    @Override
    public void sendRerouteRequest(final SoftwareConfiguration softwareConfiguration, final ShipmentId shipmentId) {
        final RestClient restClient = RestClient.builder().baseUrl(softwareConfiguration.getUrl()).build();
        final Supplier<ResponseEntity<RerouteProcessDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> sendRerouteRequest(restClient,
                        shipmentId, softwareConfiguration.getValue()));

        final ResponseEntity<RerouteProcessDto> process = retryableSupplier.get();

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while registering reroute for shipment {}", shipmentId.getValue());
            throw new RuntimeException("Error while registering reroute for shipment");
        }

        log.info("Successfully registered rerouting {} for shipment {}", process.getBody().processId(),
                shipmentId.getValue());
    }
}
