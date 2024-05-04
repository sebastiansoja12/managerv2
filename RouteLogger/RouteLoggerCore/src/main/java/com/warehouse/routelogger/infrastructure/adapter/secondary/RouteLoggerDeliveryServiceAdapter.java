package com.warehouse.routelogger.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.SupplierCodeRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalRequestDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeliveryServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeliveryRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLoggerDeliveryServiceAdapter implements RouteLoggerDeliveryServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RouteLogRequestMapper requestMapper = getMapper(RouteLogRequestMapper.class);

    public RouteLoggerDeliveryServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logAnyDelivery(Object o) {
        final DeliveryRequestDto request = requestMapper.map((AnyDeliveryRequest) o);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getDelivery())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered delivery in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging delivery"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging delivery"));
    }

    @Override
    public void logDepotCode(Object o) {
        final DeliveryRequestDto request = requestMapper.map((AnyDeliveryRequest) o);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getDepotCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered delivery in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging delivery"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging delivery"));
    }

    @Override
    public void logRequest(Object o) {
        final TerminalRequestDto request = requestMapper.map((Request) o);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getTerminalRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered request in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging request"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging request"));
    }

    @Override
    public void logSupplierCode(Object o) {
        final SupplierCodeRequestDto request = requestMapper.map((SupplierCodeRequest) o);
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
