package com.warehouse.routelogger.domain.port.primary;

import com.warehouse.routelogger.domain.model.*;

public interface RouteLoggerPort {

    void logAnyDelivery(final AnyDeliveryRequest request);

    void logDepotCode(final DepotCodeRequest request);

    void logRequest(final Request request);

    void logSupplierCode(final SupplierCodeRequest request);

    void logTerminalId(final TerminalLogRequest request);

    void logVersion(final VersionLogRequest request);

    void logUsername(final UsernameLogRequest request);

    void logRejectTrackerRequest(final RejectTrackerRequest request);

    void logDeviceInformation(final DeviceInformationRequest request);
}
