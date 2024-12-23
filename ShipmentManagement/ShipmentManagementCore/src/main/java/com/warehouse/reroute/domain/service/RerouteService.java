package com.warehouse.reroute.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.vo.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.vo.GeneratedToken;

public interface RerouteService {

    RerouteResponse createRerouteToken(RerouteRequest request, GeneratedToken generatedToken);

    void deleteToken(RerouteToken rerouteToken);

    RerouteToken findByToken(Token token);

    RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId);

    void invalidateToken(final ShipmentId shipmentId);
}
