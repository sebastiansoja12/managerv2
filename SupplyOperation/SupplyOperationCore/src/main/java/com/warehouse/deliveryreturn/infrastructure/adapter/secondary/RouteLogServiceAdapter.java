package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.routelog.request.DeliveryReturnLogRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryRouteRequestMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceAdapter implements RouteLogServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;
    
    private final RestClient restClient;
    
    private final DeliveryRouteRequestMapper requestMapper = getMapper(DeliveryRouteRequestMapper.class);

    public RouteLogServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder()
                .baseUrl(routeTrackerLogProperties.getUrl())
                .build();
    }

    @Override
    public void logDeliverReturn(DeliveryReturnRouteRequest deliveryReturnRouteRequest) {
        final List<DeliveryReturnLogRequestDto> request = requestMapper.map(deliveryReturnRouteRequest);

        request.stream()
                .peek(this::logDeliverReturnInformation)
                .forEach(this::logDeliverReturn);
    }

    private void logDeliverReturnInformation(DeliveryReturnLogRequestDto logRequest) {
        log.info("Logging delivery return in tracker module for parcel: {}", logRequest.getParcelId());
    }

    private void logDeliverReturn(DeliveryReturnLogRequestDto request) {
        restClient.post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getDeliveryReturnRequest())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered return in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging return"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging return"));
    }
}
