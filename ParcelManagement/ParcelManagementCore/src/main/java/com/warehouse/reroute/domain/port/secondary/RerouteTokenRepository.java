package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;

public interface RerouteTokenRepository {

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);

    RerouteToken findByToken(Token token);

    RerouteToken saveReroutingToken(RerouteToken rerouteToken);

    void deleteByToken(RerouteToken token);
}
