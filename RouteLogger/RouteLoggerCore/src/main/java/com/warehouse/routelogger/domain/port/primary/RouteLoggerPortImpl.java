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

    @Override
    public void logAnyDelivery(AnyDeliveryRequest request) {
        routeLoggerDeliveryServicePort.logAnyDelivery(request);
    }

    @Override
    public void logDepotCode(DepotCodeRequest request) {
        routeLoggerDepotCodeServicePort.logDepotCode(request);
    }

    @Override
    public void logRequest(Request request) {
        routeLoggerRequestServicePort.logRequest(request);
    }

    @Override
    public void logSupplierCode(SupplierCodeRequest request) {
        routeLoggerSupplierCodeServicePort.logSupplierCode(request);
    }

    @Override
    public void logTerminalId(TerminalLogRequest request) {
        routeLoggerTerminalServicePort.logTerminalId(request);
    }

    @Override
    public void logVersion(VersionLogRequest request) {
        routeLoggerVersionServicePort.logVersion(request);
    }

    @Override
    public void logUsername(UsernameLogRequest request) {
        routeLoggerUsernameServicePort.logUsername(request);
    }
}
