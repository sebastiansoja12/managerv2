package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.function.Supplier;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteProcessDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ShipmentCreatedRequest;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceAdapter implements RouteLogServicePort {

    private final Retry retry;

    public RouteLogServiceAdapter(final RetryConfig retryConfig) {
        this.retry = Retry.of("routeProcessRetry", retryConfig);
    }

	private ResponseEntity<RouteProcessDto> sendRouteProcessRequest(final RestClient restClient,
			final ShipmentId shipmentId, final String url) {
        log.info("Trying to send route process request for shipment {}", shipmentId.getValue());
        final ShipmentCreatedRequest request = new ShipmentCreatedRequest(shipmentId, null);
        return restClient
                .post()
                .uri(url)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(RouteProcessDto.class);
    }

    @Override
	public RouteProcess notifyShipmentCreated(final ShipmentId shipmentId,
                                              final SoftwareConfiguration softwareConfiguration) {
        final RestClient restClient = RestClient.builder().baseUrl(softwareConfiguration.getUrl()).build();
        final Supplier<ResponseEntity<RouteProcessDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> sendRouteProcessRequest(restClient, 
                        shipmentId, softwareConfiguration.getValue()));

        final ResponseEntity<RouteProcessDto> process = retryableSupplier.get();

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while registering route for shipment {}", shipmentId.getValue());
            throw new RuntimeException("Error while registering route for shipment");
        }

        log.info("Successfully registered route {} for shipment {}", process.getBody().getProcessId(),
                shipmentId.getValue());

        return RouteProcess.from(shipmentId, process.getBody().getProcessId());
    }
}
