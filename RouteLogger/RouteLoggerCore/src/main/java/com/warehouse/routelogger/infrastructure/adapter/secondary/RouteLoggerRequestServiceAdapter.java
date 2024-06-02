package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerRequestServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.mapstruct.factory.Mappers.getMapper;


@Slf4j
public class RouteLoggerRequestServiceAdapter implements RouteLoggerRequestServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RouteLogRequestMapper requestMapper = getMapper(RouteLogRequestMapper.class);

    public RouteLoggerRequestServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logRequest(Request request) {
        final TerminalRequestDto terminalRequest = requestMapper.map(request);
        restClient
                .post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getTerminalRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .body(terminalRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered request in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging request"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging request"));
    }
}
