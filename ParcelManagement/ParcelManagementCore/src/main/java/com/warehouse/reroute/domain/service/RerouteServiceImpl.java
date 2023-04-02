package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.port.secondary.ParcelPort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.domain.model.Token;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final RerouteTokenPort rerouteTokenPort;

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
    public RerouteTokenResponse findByToken(Token token) {
        return rerouteTokenPort.findByToken(token);
    }

    @Override
    public RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest) {
        return rerouteTokenPort.sendReroutingInformation(rerouteRequest);
    }

    @Override
    public RerouteTokenResponse loadByTokenAndParcelId(Token token, ParcelId aParcelId) {
        return rerouteTokenPort.loadByTokenAndParcelId(token, aParcelId);
    }

    public Token token(Integer value) {
        return Token.builder()
                .value(value)
                .build();
    }
}
