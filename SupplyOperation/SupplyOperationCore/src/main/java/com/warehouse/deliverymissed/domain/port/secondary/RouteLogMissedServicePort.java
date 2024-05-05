package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.terminal.request.TerminalRequest;

public interface RouteLogMissedServicePort {
    void logRouteLogMissedDelivery(final DeliveryMissed deliveryMissed);

    void logDepotCodeMissedDelivery(final DeliveryMissed deliveryMissed);

    void logRequest(final TerminalRequest terminalRequest, final String requestAsJson);

    void logSupplierCode(final DeliveryMissed deliveryMissed);

    void logTerminalId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);
}
