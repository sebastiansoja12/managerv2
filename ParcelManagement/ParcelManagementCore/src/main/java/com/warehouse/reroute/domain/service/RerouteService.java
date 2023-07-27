package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

public interface RerouteService {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);

    ParcelUpdateResponse update(Parcel parcel, RerouteToken rerouteToken);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);
}
