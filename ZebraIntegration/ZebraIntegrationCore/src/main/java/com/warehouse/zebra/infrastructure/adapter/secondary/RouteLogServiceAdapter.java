package com.warehouse.zebra.infrastructure.adapter.secondary;

import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.LogStatus;
import com.warehouse.zebra.domain.vo.RouteProcess;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ZebraInitializeRequestDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ProcessTypeDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.RouteLogResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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
        final ZebraInitializeRequestDto request = ZebraInitializeRequestDto.builder()
                .parcelId(parcelId)
                .processType(ProcessTypeDto.CREATED)
                .build();
        return restClient
                .post()
                .uri("/v2/api/routes/{initialize}", routeTrackerLogProperties.getZebraInitialize())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
				.exchange((req, res) -> {
					final HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(res.getStatusCode().value());
					if (httpStatusCode.is2xxSuccessful()) {
						return new RouteProcess(parcelId, LogStatus.OK);
					}
					if (httpStatusCode.is5xxServerError()) {
						return new RouteProcess(parcelId, LogStatus.ERROR);
					} else {
						return new RouteProcess(parcelId, LogStatus.NOT_OK);
					}
				});
    }
}
