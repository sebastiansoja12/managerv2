package com.warehouse.reroute.application.port.secondary;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;

public interface RerouteNotificationServicePort {

    void sendRerouteToken(final ReroutableShipment shipment, final RerouteToken rerouteToken);
}
