package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.domain.vo.RerouteResponse;

public interface RerouteTokenPort {

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);

    RerouteParcelResponse update(RerouteParcelRequest request);
}
