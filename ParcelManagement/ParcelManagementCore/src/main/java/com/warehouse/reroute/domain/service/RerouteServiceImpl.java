package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.*;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final RerouteTokenServicePort rerouteTokenServicePort;

    private final RerouteTokenRepository rerouteTokenRepository;

    private final ParcelRepository parcelRepository;

    private final PathFinderServicePort pathFinderServicePort;

    @Override
    public ParcelUpdateResponse update(Parcel parcel, RerouteToken rerouteToken) {
        final City city = pathFinderServicePort.determineNewDeliveryDepot(parcel);

        final ParcelUpdateResponse parcelUpdateResponse = parcelRepository.updateParcel(parcel, city);

        rerouteTokenRepository.deleteByToken(rerouteToken);

        return parcelUpdateResponse;
    }

    @Override
    public RerouteToken findByToken(Token token) {
        return rerouteTokenRepository.findByToken(token);
    }

    @Override
    public RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest) {
        return rerouteTokenServicePort.sendReroutingInformation(rerouteRequest);
    }

    @Override
    public RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId) {
        return rerouteTokenRepository.loadByTokenAndParcelId(token, parcelId);
    }

    public Token token(Integer value) {
        return Token.builder()
                .value(value)
                .build();
    }
}
