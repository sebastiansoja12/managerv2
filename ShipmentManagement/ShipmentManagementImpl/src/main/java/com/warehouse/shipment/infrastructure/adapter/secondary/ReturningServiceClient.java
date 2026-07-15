package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ChangeReturnStatusApiRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;
import com.warehouse.tools.returning.ReturnProperties;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
public class ReturningServiceClient implements ReturningServicePort {

    private final ExternalFeignClient externalFeignClient;

    private final ReturnProperties returnProperties;

    public ReturningServiceClient(final ExternalFeignClient externalFeignClient,
                                  final ReturnProperties returnProperties) {
        this.externalFeignClient = externalFeignClient;
        this.returnProperties = returnProperties;
    }

    @Override
    public ShipmentReturnDetails getReturn(final ReturnId returnId) {
        log.info("Loading return {} from returning manager", returnId.getId());
        try {
            final ResponseEntity<ReturnPackageApi> response = this.externalFeignClient.getReturn(returnUri(returnId));
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new TechnicalException(response.getStatusCode(),
                        "Returning manager returned an empty response for return " + returnId.getId());
            }
            return ReturnResponseMapper.map(response.getBody());
        } catch (final FeignException exception) {
            final HttpStatusCode status = exception.status() >= 400 && exception.status() <= 599
                    ? HttpStatusCode.valueOf(exception.status())
                    : HttpStatus.BAD_GATEWAY;
            log.error("Could not load return {} from returning manager", returnId.getId(), exception);
            throw new TechnicalException(status, "Could not load return " + returnId.getId());
        }
    }

    @Override
    public Optional<ShipmentReturnDetails> findReturnByShipmentId(final ShipmentId shipmentId) {
        final URI uri = URI.create(returnProperties.getUrl() + returnProperties.getEndpoint()
                + "/shipment/" + shipmentId.getValue());
        try {
            final ResponseEntity<ReturnPackageApi> response = this.externalFeignClient.getReturn(uri);
            return Optional.ofNullable(response.getBody()).map(ReturnResponseMapper::map);
        } catch (final FeignException exception) {
            log.warn("Could not load optional return details for shipment {}", shipmentId.getValue(), exception);
            return Optional.empty();
        }
    }

    @Override
    public ShipmentReturnPage getReturns(
            final DepartmentCode departmentCode, final int page, final int size) {
        log.info("Loading returns for department {} from returning manager", departmentCode.value());
        try {
            final ResponseEntity<ReturnPageApi> response = this.externalFeignClient.getReturns(
                    returnsUri(departmentCode, page, size));
            return ReturnResponseMapper.map(response.getBody());
        } catch (final FeignException exception) {
            log.error("Could not load returns for department {}", departmentCode.value(), exception);
            throw new TechnicalException(HttpStatus.BAD_GATEWAY,
                    "Could not load returns for department " + departmentCode.value());
        }
    }

    @Override
    public void startProcessing(final ShipmentId shipmentId) {
        changeReturnStatus(shipmentId, "process", ReturnStatus.PROCESSING);
    }

    @Override
    public void complete(final ShipmentId shipmentId) {
        changeReturnStatus(shipmentId, "complete", ReturnStatus.COMPLETED);
    }

    private URI returnUri(final ReturnId returnId) {
        return URI.create(returnProperties.getUrl() + returnProperties.getEndpoint() + "/" + returnId.getId());
    }

    private URI returnsUri(final DepartmentCode departmentCode, final int page, final int size) {
        return UriComponentsBuilder.fromUriString(returnProperties.getUrl() + returnProperties.getEndpoint())
                .queryParam("departmentCode", departmentCode.value())
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .encode()
                .toUri();
    }

    private void changeReturnStatus(
            final ShipmentId shipmentId, final String action, final ReturnStatus returnStatus) {
        final URI uri = URI.create(returnProperties.getUrl() + returnProperties.getEndpoint() + "/" + action);
        try {
            this.externalFeignClient.changeReturnStatus(
                    uri,
                    new ChangeReturnStatusApiRequest(new ShipmentIdDto(shipmentId.getValue()), returnStatus.name()));
        } catch (final FeignException exception) {
            log.error("Could not change return status to {} for shipment {}",
                    returnStatus, shipmentId.getValue(), exception);
            throw new TechnicalException(HttpStatus.BAD_GATEWAY,
                    "Could not change return status for shipment " + shipmentId.getValue());
        }
    }
}
