package com.warehouse.routelogger.domain.port.primary;


import com.warehouse.routelogger.domain.model.*;
import com.warehouse.routelogger.domain.port.secondary.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLoggerPortImpl implements RouteLoggerPort {

    private final RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort;

    private final RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort;

    private final RouteLoggerTerminalServicePort routeLoggerTerminalServicePort;

    private final RouteLoggerVersionServicePort routeLoggerVersionServicePort;

    private final RouteLoggerUsernameServicePort routeLoggerUsernameServicePort;

    private final RouteLoggerDepotCodeServicePort routeLoggerDepotCodeServicePort;

    private final RouteLoggerRequestServicePort routeLoggerRequestServicePort;

    private final RouteLoggerDeviceInformationServicePort routeLoggerDeviceInformationServicePort;

    @Override
    public void logAnyDelivery(final AnyDeliveryRequest request) {
        routeLoggerDeliveryServicePort.logAnyDelivery(request);
    }

    @Override
    public void logDepotCode(final DepotCodeRequest request) {
        routeLoggerDepotCodeServicePort.logDepotCode(request);
    }

    @Override
    public void logRequest(final Request request) {
        routeLoggerRequestServicePort.logRequest(request);
    }

    @Override
    public void logSupplierCode(SupplierCodeRequest request) {
        routeLoggerSupplierCodeServicePort.logSupplierCode(request);
    }

    @Override
    public void logTerminalId(final TerminalLogRequest request) {
        routeLoggerTerminalServicePort.logTerminalId(request);
    }

    @Override
    public void logVersion(final VersionLogRequest request) {
        routeLoggerVersionServicePort.logVersion(request);
    }

    @Override
    public void logUsername(final UsernameLogRequest request) {
        routeLoggerUsernameServicePort.logUsername(request);
    }

    @Override
    public void logRejectTrackerRequest(final RejectTrackerRequest request) {
        // TODO log in tracker
    }

    @Override
    public void logDeviceInformation(final DeviceInformationRequest request) {
        routeLoggerDeviceInformationServicePort.logDeviceInformation(request);
    }
}
