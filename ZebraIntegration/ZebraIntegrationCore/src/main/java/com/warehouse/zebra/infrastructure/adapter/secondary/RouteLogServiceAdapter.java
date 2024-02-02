package com.warehouse.zebra.infrastructure.adapter.secondary;

import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.RouteProcess;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ParcelIdDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.RouteProcessDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.RouteLogResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.mapstruct.factory.Mappers.getMapper;

@Slf4j
public class RouteLogServiceAdapter implements RouteLogServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RouteLogResponseMapper responseMapper = getMapper(RouteLogResponseMapper.class);


    public RouteLogServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public RouteProcess initializeProcess(Long parcelId) {
        final ParcelIdDto parcelIdRequest = ParcelIdDto.builder().value(parcelId).build();
        final ResponseEntity<RouteProcessDto> responseEntity = restClient
                .post()
                .uri("/v2/api/routes/test/{initialize}", routeTrackerLogProperties.getInitialize())
                .body(parcelIdRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.trace("Successfully registered process in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging process"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging process"))
                .toEntity(RouteProcessDto.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Could not register process!");
        }
        return responseMapper.map(responseEntity.getBody());
    }
}
