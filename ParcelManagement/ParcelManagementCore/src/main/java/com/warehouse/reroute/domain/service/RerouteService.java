package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

public interface RerouteService {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);

    ParcelUpdateResponse update(UpdateParcelRequest parcelRequest);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Token token, ParcelId aParcelId);
}
