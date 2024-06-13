package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.terminal.request.TerminalRequest;

public interface RouteLogReturnServicePort {
    void logRouteLogReturnDelivery(final DeliveryReturnRouteRequest deliveryReturnRouteRequest);

    void logDepotCodeReturnDelivery(final DeliveryReturnRequest deliveryReturnRequest);

    void logRequest(final TerminalRequest terminalRequest, final String requestAsJson);

    void logSupplierCode(final DeliveryReturn deliveryReturn);

    void logTerminalId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);
}
