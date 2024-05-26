package com.warehouse.routelogger.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.model.DepotCodeRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDepotCodeServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeliveryRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RouteLoggerDepotCodeServiceAdapter implements RouteLoggerDepotCodeServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RouteLogRequestMapper requestMapper = getMapper(RouteLogRequestMapper.class);

    public RouteLoggerDepotCodeServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }


    @Override
    public void logDepotCode(DepotCodeRequest depotCodeRequest) {
        final DeliveryRequestDto request = requestMapper.mapToDepotCodeRequest(depotCodeRequest);
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
}
