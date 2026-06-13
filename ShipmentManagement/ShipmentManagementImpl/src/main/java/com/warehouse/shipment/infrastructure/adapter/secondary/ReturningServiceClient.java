package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPageApi;
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
}
