package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final RerouteTokenServicePort rerouteTokenServicePort;

    private final RerouteTokenRepository rerouteTokenRepository;

    @Override
    public void deleteToken(RerouteToken rerouteToken) {
        rerouteTokenRepository.deleteByToken(rerouteToken);
    }

    @Override
    public RerouteToken findByToken(Token token) {
        return rerouteTokenRepository.findByToken(token);
    }

    @Override
    public RerouteResponse createRerouteToken(RerouteToken rerouteToken) {
        final RerouteToken token = rerouteTokenRepository.saveReroutingToken(rerouteToken);

        rerouteTokenServicePort.sendReroutingInformation(token);

        return RerouteResponse.builder()
                .parcelId(token.getParcelId())
                .token(token.getToken())
                .build();
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
