package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeviceInformationServicePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@Slf4j
public class RouteLoggerDeviceInformationServiceMockAdapter implements RouteLoggerDeviceInformationServicePort {


    @Override
    public void logDeviceInformation(final DeviceInformationRequest request) {
        log.info("Mocking logging device in tracker: Device information request: {}", request);
    }
}
