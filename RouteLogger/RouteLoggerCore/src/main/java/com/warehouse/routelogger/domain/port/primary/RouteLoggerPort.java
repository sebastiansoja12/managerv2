package com.warehouse.routelogger.domain.port.primary;

import com.warehouse.routelogger.domain.model.*;

public interface RouteLoggerPort {

    void logAnyDelivery(AnyDeliveryRequest request);

    void logDepotCode(AnyDeliveryRequest request);

    void logRequest(Request request);

    void logSupplierCode(SupplierCodeRequest request);

    void logTerminalId(TerminalLogRequest request);

    void logVersion(VersionLogRequest request);
}
