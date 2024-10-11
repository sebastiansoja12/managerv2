package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteProcessDto;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceAdapter implements RouteLogServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RestClient restClient;

    private final Retry retry;

    public RouteLogServiceAdapter(final RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(2))
                .retryExceptions(RuntimeException.class)
                .writableStackTraceEnabled(true)
                .build();

        this.retry = Retry.of("routeProcessRetry", config);
    }

    @Override
    public RouteProcess initializeRouteProcess(final ShipmentId shipmentId) {
        final Supplier<ResponseEntity<RouteProcessDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> sendRouteProcessRequest(shipmentId));

        final ResponseEntity<RouteProcessDto> process = retryableSupplier.get();
        
		if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while registering route for shipment {}", shipmentId.getValue());
            throw new RuntimeException("Error while registering route for shipment");
		}

        log.info("Successfully registered route {} for shipment {}", process.getBody().getProcessId(),
                shipmentId.getValue());

        return RouteProcess.from(shipmentId, process.getBody() != null ? process.getBody().getProcessId() : null);
    }

    private ResponseEntity<RouteProcessDto> sendRouteProcessRequest(final ShipmentId shipmentId) {
        log.info("Trying to send route process request for shipment {}", shipmentId.getValue());
        return restClient
                .post()
                .uri("/v2/api/routes/{initialize}", routeTrackerLogProperties.getInitialize())
                .body(shipmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(RouteProcessDto.class);
    }
}
