package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.PersonChangedRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecord;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteProcessDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ShipmentCreatedRequest;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceClient implements RouteLogServicePort {

    private final Retry retry;

    private final ExternalFeignClient externalFeignClient;

    public RouteLogServiceClient(final RetryConfig retryConfig,
                                 final ExternalFeignClient externalFeignClient) {
        this.retry = Retry.of("routeProcessRetry", retryConfig);
        this.externalFeignClient = externalFeignClient;
    }

	private ResponseEntity<RouteProcessDto> sendRouteProcessRequest(final ShipmentId shipmentId,
                                                                    final SoftwareConfiguration softwareConfiguration) {
        log.info("Trying to send route process request for shipment {}", shipmentId.getValue());
        final ShipmentCreatedRequest request = new ShipmentCreatedRequest(shipmentId, null);
        return externalFeignClient.createShipmentRoute(
                URI.create(softwareConfiguration.getUrl() + softwareConfiguration.getValue()),
                request
        );
    }

    // TODO
    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentCreated(final ShipmentId shipmentId,
                                                                 final SoftwareConfiguration softwareConfiguration) {
        try {
            final Supplier<ResponseEntity<RouteProcessDto>> retryableSupplier = Retry
                    .decorateSupplier(retry, () -> sendRouteProcessRequest(shipmentId, softwareConfiguration));

            final ResponseEntity<RouteProcessDto> process = retryableSupplier.get();

            if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
                log.error("Error while registering route for shipment {}", shipmentId.getValue());
                return Result.failure(ErrorCode.ROUTE_TRACKER_SERVICE_ERROR);
            }

            log.info("Successfully registered route {} for shipment {}", process.getBody().getProcessId(),
                    shipmentId.getValue());
            return Result.success(RouteProcess.from(shipmentId, new ProcessId(process.getBody().getProcessId()), "", ""));
        } catch (final RetryableException e) {
            log.error("RetryableException while registering route for shipment {}: {}",
                    shipmentId.getValue(), e.getMessage());
            return Result.failure(ErrorCode.ROUTE_TRACKER_SERVICE_NOT_AVAILABLE);
        } catch (final FeignException e) {
            log.error("FeignException while registering route for shipment {}: {}",
                    shipmentId.getValue(), e.getMessage());
            return Result.failure(ErrorCode.ROUTE_TRACKER_SERVICE_ERROR);
        }
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

        final ResponseEntity<RouteLogRecord> responseEntity = externalFeignClient.updateRoutePerson(
                URI.create(softwareConfiguration.getUrl() + softwareConfiguration.getValue()
                        + "/persons?personType=" + person.getType().name()),
                softwareConfiguration.getApiKey(),
                request
        );

        return RouteProcess.from(responseEntity, shipmentId);
    }

    @Override
    public void notifyShipmentUpdated(final ShipmentSnapshot snapshot) {

    }

}
