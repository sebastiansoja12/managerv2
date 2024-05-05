package com.warehouse.routelogger.domain.port.primary;


import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.domain.model.TerminalLogRequest;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeliveryServicePort;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerSupplierCodeServicePort;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerTerminalServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLoggerPortImpl implements RouteLoggerPort {

    private final RouteLoggerDeliveryServicePort routeLoggerDeliveryServicePort;

    private final RouteLoggerSupplierCodeServicePort routeLoggerSupplierCodeServicePort;

    private final RouteLoggerTerminalServicePort routeLoggerTerminalServicePort;

    @Override
    public void logAnyDelivery(AnyDeliveryRequest request) {
        routeLoggerDeliveryServicePort.logAnyDelivery(request);
    }

    @Override
    public void logDepotCode(AnyDeliveryRequest request) {
        routeLoggerDeliveryServicePort.logDepotCode(request);
    }

    @Override
    public void logRequest(Request request) {
        routeLoggerDeliveryServicePort.logRequest(request);
    }

    @Override
    public void logSupplierCode(SupplierCodeRequest request) {
        routeLoggerSupplierCodeServicePort.logSupplierCode(request);
    }

    @Override
    public void logTerminalId(TerminalLogRequest request) {
        routeLoggerTerminalServicePort.logTerminalId(request);
    }
}
