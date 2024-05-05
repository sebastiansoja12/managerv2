package com.warehouse.routelogger.domain.port.primary;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.domain.model.TerminalLogRequest;

public interface RouteLoggerPort {

    void logAnyDelivery(AnyDeliveryRequest request);

    void logDepotCode(AnyDeliveryRequest request);

    void logRequest(Request request);

    void logSupplierCode(SupplierCodeRequest request);

    void logTerminalId(TerminalLogRequest request);
}
