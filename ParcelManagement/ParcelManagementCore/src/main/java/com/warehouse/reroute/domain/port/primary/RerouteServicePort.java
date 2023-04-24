package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

public interface RerouteServicePort {

    ParcelUpdateResponse update(UpdateParcelRequest request);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Token token, ParcelId aParcelId);

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);
}
