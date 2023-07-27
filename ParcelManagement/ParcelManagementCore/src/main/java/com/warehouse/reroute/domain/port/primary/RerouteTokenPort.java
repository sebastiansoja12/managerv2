package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;

public interface RerouteTokenPort {

    ParcelUpdateResponse update(UpdateParcelRequest request);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);
}
