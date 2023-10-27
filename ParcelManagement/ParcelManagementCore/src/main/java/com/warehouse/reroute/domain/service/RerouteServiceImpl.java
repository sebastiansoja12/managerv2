package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.MailServicePort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.GeneratedToken;
import com.warehouse.reroute.domain.vo.RerouteProcessor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteServiceImpl implements RerouteService {

    private final MailServicePort mailServicePort;

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
    public RerouteResponse createRerouteToken(RerouteRequest request, GeneratedToken generatedToken) {
        final RerouteToken rerouteToken = rerouteTokenRepository.saveReroutingToken(
            new RerouteProcessor(request.getEmail(), request.getParcelId(), generatedToken)
        );

        mailServicePort.sendReroutingInformation(rerouteToken);

        return new RerouteResponse(rerouteToken.getParcelId(), rerouteToken.getToken());
    }

    @Override
    public RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId) {
        return rerouteTokenRepository.loadByTokenAndParcelId(token, parcelId);
    }
}
