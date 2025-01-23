package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeviceInformationServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeviceInformationRequestDto;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


@AllArgsConstructor
public class RouteLoggerDeviceInformationServiceAdapter implements RouteLoggerDeviceInformationServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    @Override
    public void logDeviceInformation(final DeviceInformationRequest request) {
        final RestClient restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
        final DeviceInformationRequestDto deviceInformationRequest = DeviceInformationRequestDto.from(request);
        restClient
                .post()
                .uri("/v2/api/routes/device-information")
                .body(deviceInformationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Void.class);
    }
}
