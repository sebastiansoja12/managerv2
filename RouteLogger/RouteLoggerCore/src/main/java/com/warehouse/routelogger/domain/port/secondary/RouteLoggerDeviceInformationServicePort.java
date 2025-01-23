package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;

public interface RouteLoggerDeviceInformationServicePort {
    void logDeviceInformation(final DeviceInformationRequest request);
}
