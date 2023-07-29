package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;

public interface RerouteService {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);

    TokenDeletionStatus deleteToken(RerouteToken rerouteToken);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);
}
