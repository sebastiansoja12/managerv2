package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerSupplierCodeServicePort;

import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.SupplierCodeRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.mapstruct.factory.Mappers.getMapper;

@Slf4j
public class RouteLoggerSupplierCodeServiceAdapter implements RouteLoggerSupplierCodeServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RouteLogRequestMapper requestMapper = getMapper(RouteLogRequestMapper.class);

    public RouteLoggerSupplierCodeServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logSupplierCode(SupplierCodeRequest supplierCodeRequest) {
        final SupplierCodeRequestDto request = requestMapper.map(supplierCodeRequest);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getSupplierCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered supplier code in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging supplier code"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging supplier code"));
    }
}
