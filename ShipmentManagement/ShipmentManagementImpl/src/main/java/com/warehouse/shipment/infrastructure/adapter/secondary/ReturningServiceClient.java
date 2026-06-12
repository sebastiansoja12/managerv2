package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ShipmentReturnedCommand;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OutputRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;
import com.warehouse.tools.returning.ReturnProperties;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

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
    public void notifyShipmentReturn(final ShipmentSnapshot snapshot) {
        log.info("Sending request to returning manager for shipment {}", snapshot.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(snapshot);
        this.externalFeignClient.processReturn(returnUri(), request);
    }

    @Override
    public Map<ShipmentId, ReturnId> shipmentReturnCommand(final ShipmentReturnedCommand shipmentReturnedCommand) {
        log.info("Sending request to returning manager for shipment {}", shipmentReturnedCommand.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(shipmentReturnedCommand);
        final ResponseEntity<ReturnResponseApi> response = this.externalFeignClient.processReturn(returnUri(), request);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Error while sending request to returning manager for shipment {}", shipmentReturnedCommand.shipmentId());
            throw new RuntimeException("Error while sending request to returning manager for shipment");
        }

        final Map<ShipmentIdDto, ReturnIdDto> responseMap = response.getBody()
                .processReturn()
                .stream()
                .collect(Collectors.toMap(
                        ProcessReturnDto::shipmentId,
                        ProcessReturnDto::returnId
                ));


        return responseMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> new ShipmentId(entry.getKey().value()),
                        entry -> new ReturnId(entry.getValue().value())
                ));
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
    public ShipmentReturnPage getReturns(
            final DepartmentCode departmentCode, final int page, final int size) {
        log.info("Loading returns for department {} from returning manager", departmentCode.value());
        try {
            final ResponseEntity<ReturnPageApi> response = this.externalFeignClient.getReturns(
                    returnsUri(departmentCode, page, size));
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new TechnicalException(response.getStatusCode(),
                        "Returning manager returned an empty response for department " + departmentCode.value());
            }
            return ReturnResponseMapper.map(response.getBody());
        } catch (final FeignException exception) {
            final HttpStatusCode status = exception.status() >= 400 && exception.status() <= 599
                    ? HttpStatusCode.valueOf(exception.status())
                    : HttpStatus.BAD_GATEWAY;
            log.error("Could not load returns for department {}", departmentCode.value(), exception);
            throw new TechnicalException(status,
                    "Could not load returns for department " + departmentCode.value());
        }
    }

    @Override
    public void notifyShipmentUpdated(final ShipmentSnapshot snapshot) {
        log.info("Updating shipment in return manager {}", snapshot.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(snapshot);
        this.externalFeignClient.processReturn(returnUri(), request);
    }

    @Override
    public void notifyShipmentReturnCompleted(final ShipmentSnapshot snapshot) {
        log.info("Finishing shipment return in return manager {}", snapshot.shipmentId().toString());
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ShipmentIdDto(snapshot.shipmentId().getValue()), "COMPLETED"
        );
        this.externalFeignClient.completeReturn(completeReturnUri(), request);
    }

    private URI returnUri() {
        return URI.create(returnProperties.getUrl() + returnProperties.getEndpoint());
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

    private URI completeReturnUri() {
        return URI.create(returnProperties.getUrl() + returnProperties.getEndpoint() + "/complete");
    }
}
