package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;
import java.util.function.Supplier;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.Person;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.PersonChangedRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecord;
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

    // TODO
    @Override
	public RouteProcess notifyShipmentCreated(final ShipmentId shipmentId,
                                              final SoftwareConfiguration softwareConfiguration) {
        final RestClient restClient = buildRestClient(softwareConfiguration);
        final Supplier<ResponseEntity<RouteProcessDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> sendRouteProcessRequest(restClient, 
                        shipmentId, softwareConfiguration.getValue()));

        final ResponseEntity<RouteProcessDto> process;

        try {
            process = retryableSupplier.get();
        } catch (final HttpClientErrorException e) {
            return RouteProcess.from(shipmentId, null, e.getMessage(), e.getResponseBodyAsString());
        }

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while registering route for shipment {}", shipmentId.getValue());
            throw new RuntimeException("Error while registering route for shipment");
        }

        log.info("Successfully registered route {} for shipment {}", process.getBody().getProcessId(),
                shipmentId.getValue());

        return RouteProcess.from(shipmentId, new ProcessId(process.getBody().getProcessId()), "", "");
    }

    @Override
	public RouteProcess notifyRecipientChanged(final ShipmentId shipmentId, final Recipient recipient,
			final SoftwareConfiguration softwareConfiguration) {
        return null;
    }

    @Override
	public RouteProcess notifyPersonChanged(final ShipmentId shipmentId, final Person person,
			final SoftwareConfiguration softwareConfiguration) {
        final PersonChangedRequest request = new PersonChangedRequest(shipmentId, person);

        final RestClient restClient = buildRestClient(softwareConfiguration);

        final ResponseEntity<RouteLogRecord> responseEntity = restClient
                .put()
                .uri(URI.create(
                        softwareConfiguration.getValue()
                                + "/persons?personType=" + person.getType().name()
                ))
                .body(request)
                .header("X-API-KEY", softwareConfiguration.getApiKey())
                .retrieve()
                .toEntity(RouteLogRecord.class);

        return RouteProcess.from(responseEntity, shipmentId);
    }

    private RestClient buildRestClient(final SoftwareConfiguration softwareConfiguration) {
        return RestClient.builder()
                .baseUrl(softwareConfiguration.getUrl() + "/persons")
                .build();
    }
}
