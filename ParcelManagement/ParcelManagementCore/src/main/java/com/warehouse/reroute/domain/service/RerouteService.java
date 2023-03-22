package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.domain.model.Token;

public interface RerouteService {

    RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest);

    ParcelUpdateResponse update(UpdateParcelRequest parcelRequest);

    RerouteTokenResponse findByToken(Token token);

    RerouteTokenResponse loadByTokenAndParcelId(Token token, ParcelId aParcelId);


}
