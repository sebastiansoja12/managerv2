package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.*;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final RerouteTokenServicePort rerouteTokenServicePort;

    private final RerouteTokenRepository rerouteTokenRepository;

    @Override
    public TokenDeletionStatus deleteToken(RerouteToken rerouteToken) {
        rerouteTokenRepository.deleteByToken(rerouteToken);
        return new TokenDeletionStatus("OK");
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
