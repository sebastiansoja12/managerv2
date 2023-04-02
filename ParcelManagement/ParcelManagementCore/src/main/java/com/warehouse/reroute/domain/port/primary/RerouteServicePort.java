package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

public interface RerouteServicePort {

    ParcelUpdateResponse update(UpdateParcelRequest request);

    RerouteTokenResponse findByToken(Token token);

    RerouteTokenResponse loadByTokenAndParcelId(Token token, ParcelId aParcelId);

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);
}
