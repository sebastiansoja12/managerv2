package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.UsernameLogRequestDto;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.model.UsernameLogRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerUsernameServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLoggerUsernameServiceAdapter implements RouteLoggerUsernameServicePort {

    private final RestClient restClient;
    private final RouteTrackerLogProperties routeTrackerLogProperties;
    private final RouteLogRequestMapper requestMapper = Mappers.getMapper(RouteLogRequestMapper.class);

    public RouteLoggerUsernameServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logUsername(UsernameLogRequest usernameLogRequest) {
        final UsernameLogRequestDto request = requestMapper.map(usernameLogRequest);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered device version in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging device version"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging device version"));
    }
}
