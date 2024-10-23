package com.warehouse.reroute.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.domain.vo.RerouteResponse;

public interface RerouteTokenPort {

    RerouteToken findByToken(final Token token);

    RerouteToken loadByTokenAndParcelId(final Integer token, final Long parcelId);

    RerouteResponse sendReroutingInformation(final RerouteRequest rerouteRequest);

    RerouteParcelResponse update(final RerouteParcelRequest request);

    void rerouteShipment(final ShipmentId shipmentId);
}
