package com.warehouse.shipment.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteProcessDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.ParcelId;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.RouteProcessMapper;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceAdapter implements RouteLogServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RestClient restClient;

    private final RouteProcessMapper mapper = getMapper(RouteProcessMapper.class);

    public RouteLogServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public RouteProcess initializeRouteProcess(ParcelId parcelId) {
        final ResponseEntity<RouteProcessDto> process = restClient
                .post()
				.uri("/v2/api/routes/{initialize}", routeTrackerLogProperties.getInitialize())
                .body(parcelId)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(RouteProcessDto.class);

        if (process.getStatusCode().is2xxSuccessful()) {
            if (process.getBody() != null) {
                log.info("Successfully registered route {} for parcel {}", process.getBody().getProcessId(),
                        parcelId.getValue());
            }
        } else {
            log.error("Error while registering route for parcel {}", parcelId.getValue());
            throw new RuntimeException("Error while registering route for parcel");
        }
        return mapper.map(process.getBody());
    }
}
