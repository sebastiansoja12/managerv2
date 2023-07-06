package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;

public interface RerouteTokenServicePort {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);
}
