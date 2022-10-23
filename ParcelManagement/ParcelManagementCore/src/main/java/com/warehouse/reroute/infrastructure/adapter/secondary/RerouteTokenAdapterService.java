package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteNotification;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RequestMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ResponseMapper;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenAdapterService implements RerouteTokenPort {

    private final RequestMapper requestMapper;

    private final RerouteTokenRepository rerouteTokenRepository;

    private final MailService mailService;

    private final ResponseMapper responseMapper;

    private void buildReroutingInformation(RerouteRequest rerouteRequest, RerouteResponse rerouteResponse) {
        final Notification notification = requestMapper.map(buildNotification(rerouteRequest, rerouteResponse));
        mailService.sendNotification(notification);
    }

    @Override
    public RerouteTokenResponse findByToken(Token token) {

        final RerouteToken rerouteToken = rerouteTokenRepository.findByToken(token);

        return responseMapper.map(rerouteToken);
    }

    @Override
    public RerouteTokenResponse loadByTokenAndParcelId(Token token, ParcelId aParcelId) {

        final RerouteToken rerouteToken = rerouteTokenRepository.loadByTokenAndParcelId(token, aParcelId);

        return responseMapper.map(rerouteToken);
    }

    @Override
    public RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest) {

        final Integer token = rerouteTokenRepository.saveReroutingToken(rerouteRequest.getParcelId());

        final RerouteResponse rerouteResponse = responseMapper.map(rerouteRequest, token);

        buildReroutingInformation(rerouteRequest, rerouteResponse);

        return rerouteResponse;
    }

    public RerouteNotification buildNotification(RerouteRequest rerouteRequest, RerouteResponse rerouteResponse) {
        return RerouteNotification.builder()
                .body("Prosimy wejść w link: " + "http://localhost:4200/reroute-edit/" + rerouteRequest.getParcelId()
                        + "/" + rerouteResponse.getToken())
                .recipient(rerouteRequest.getEmail())
                .subject("Zmiana danych przesyłki: " + rerouteRequest.getParcelId())
                .build();
    }
}
