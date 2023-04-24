package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

public interface RerouteTokenPort {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);
}
