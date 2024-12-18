package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.vo.RerouteProcessor;

public interface RerouteTokenRepository {

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);

    RerouteToken findByToken(Token token);

    RerouteToken saveReroutingToken(RerouteProcessor rerouteProcessor);

    void deleteByToken(RerouteToken token);

    RerouteToken findByShipmentId(final ShipmentId shipmentId);

    void update(final RerouteToken rerouteToken);
}
