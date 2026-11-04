package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.exception.ReturnDetailsUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Slf4j
public class ReturningServiceClient implements ReturningServicePort {
    private final ReturningApiService returningApiService;

    public ReturningServiceClient(final ReturningApiService returningApiService) {
        this.returningApiService = returningApiService;
    }

    @Override
    public Optional<ReturnDetailsDto> findReturnByShipmentId(final ShipmentId shipmentId) {
        try {
            return returningApiService.findByShipmentId(shipmentId);
        } catch (final RestException | RestClientException exception) {
            log.warn("Return details unavailable for shipment {}: {}", shipmentId.getValue(), exception.getMessage());
            throw new ReturnDetailsUnavailableException("Return details are unavailable", exception);
        }
    }
}
