package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.domain.port.secondary.RouteLogServicePort;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnTrackRequestDto;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.RouteLogRequestMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.properties.Addressing;
import com.warehouse.returning.infrastructure.adapter.secondary.properties.RouteTrackerLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.mapstruct.factory.Mappers.getMapper;

@Slf4j
public class RouteLogServiceAdapter extends RestGatewaySupport implements RouteLogServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RestClient restClient;

    private final RouteLogRequestMapper requestMapper = getMapper(RouteLogRequestMapper.class);

    public RouteLogServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logReturn(ProcessReturn processReturn, String depotCode, String username) {
        final RouteLogServiceConfiguration configuration = new RouteLogServiceConfiguration(routeTrackerLogProperties);
        final ReturnTrackRequestDto request = requestMapper.map(processReturn, depotCode, username);
        logReturn(configuration, request);
    }

    private void logReturn(RouteLogServiceConfiguration configuration, ReturnTrackRequestDto request) {
        restClient
                .post()
                .uri("/v2/api/routes/test/returntrackrequest")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered return in tracker module"))
				.onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging return"))
				.onStatus(HttpStatusCode::is5xxServerError,
						(req, res) -> log.warn("Critical error while logging return"))
                .toBodilessEntity();
    }

    public static class RouteLogServiceConfiguration implements Addressing {

        private final RouteTrackerLogProperties routeTrackerLogProperties;

        public RouteLogServiceConfiguration(RouteTrackerLogProperties routeTrackerLogProperties) {
            this.routeTrackerLogProperties = routeTrackerLogProperties;
        }

        @Override
        public String getUrl() {
            return routeTrackerLogProperties.getUrl();
        }

        @Override
        public String getEndpoint() {
            return routeTrackerLogProperties.getEndpoint();
        }
    }
}
