package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.DeliveryReturnRouteRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.RouteRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.RouteResponseDto;
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
        final DeliveryReturnRouteRequestDto request = requestMapper.map(deliveryReturnRouteRequest);

        final List<RouteRequestDto> routeRequest = buildRouteRequest(request);
        final ResponseEntity<List<RouteResponseDto>> logRoute = restClient.post()
                        .uri("/v2/api/routes/test/{endpoint}", routeTrackerLogProperties.getEndpoint())
                        .body(routeRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<List<RouteResponseDto>>() {});

        if (logRoute.getStatusCode().is2xxSuccessful()) {
			if (!CollectionUtils.isEmpty(logRoute.getBody())) {
				logRoute.getBody().forEach(res -> log.info("Successfully logged route for parcel {} with route id: {}",
						res.getParcelId(), res.getId()));
			}
		}
    }

	private List<RouteRequestDto> buildRouteRequest(DeliveryReturnRouteRequestDto request) {
		return request.getDeliveryReturnRouteDetails().stream()
				.map(req -> RouteRequestDto
                        .builder()
                        .parcelId(req.getParcelId())
                        .supplierCode(request.getSupplierCode())
                        .depotCode(request.getDepotCode())
                        .build())
                .toList();

	}
}
