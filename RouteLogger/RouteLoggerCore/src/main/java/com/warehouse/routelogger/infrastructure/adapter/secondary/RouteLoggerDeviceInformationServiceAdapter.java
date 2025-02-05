package com.warehouse.routelogger.infrastructure.adapter.secondary;

import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeviceInformationServicePort;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeviceInformationRequestDto;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteLoggerDeviceInformationServiceAdapter implements RouteLoggerDeviceInformationServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;
    private final RetryTemplate retryTemplate;

    @Override
    public void logDeviceInformation(final DeviceInformationRequest request) {
        final RestClient restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
        final DeviceInformationRequestDto deviceInformationRequest = DeviceInformationRequestDto.from(request);

        retryTemplate.execute(context -> {
            restClient
                    .post()
                    .uri("/v2/api/routes/device-information")
                    .body(deviceInformationRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Void.class);
            return null;
        });
    }
}
