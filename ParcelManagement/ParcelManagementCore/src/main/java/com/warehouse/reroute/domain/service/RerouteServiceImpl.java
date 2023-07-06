package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.ParcelPort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenServicePort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final RerouteTokenServicePort rerouteTokenServicePort;

    private final ParcelPort parcelPort;

    private final RerouteTokenRepository rerouteTokenRepository;

    @Override
    public ParcelUpdateResponse update(UpdateParcelRequest parcelRequest) {

        final ParcelUpdateResponse parcelUpdateResponse = parcelPort.update(parcelRequest);

        final Token token = token(parcelRequest.getToken());

        rerouteTokenRepository.deleteByToken(token);

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
    public RerouteToken loadByTokenAndParcelId(Token token, ParcelId aParcelId) {
        return rerouteTokenRepository.loadByTokenAndParcelId(token, aParcelId);
    }

    public Token token(Integer value) {
        return Token.builder()
                .value(value)
                .build();
    }
}
