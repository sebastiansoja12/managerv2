package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.Logger;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.service.RerouteService;

import com.warehouse.reroute.domain.service.RerouteTokenGeneratorService;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Instant;

import static com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes.REROUTE_102;

@AllArgsConstructor
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteService rerouteService;

    private final ParcelReroutePort parcelReroutePort;

    private final RerouteTokenGeneratorService tokenGeneratorService;

    private final Logger logger;

    private static final long EXPIRY_TIME = 600L;


    @Override
    public RerouteToken findByToken(Token token) {
        return rerouteService.findByToken(token);
    }

    @Override
    public RerouteToken loadByTokenAndParcelId(Integer token, Long parcelId) {
        return rerouteService.loadByTokenAndParcelId(token, parcelId);
    }

    @Override
    public RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest) {
        final RerouteToken rerouteToken = buildRerouteTokenFromRequest(rerouteRequest);
        return rerouteService.createRerouteToken(rerouteToken);
    }

    private RerouteToken buildRerouteTokenFromRequest(RerouteRequest request) {
        return RerouteToken.builder()
                .parcelId(request.getParcelId())
                .createdDate(Instant.now())
                .expiryDate(Instant.now().plusSeconds(EXPIRY_TIME))
                .token(tokenGeneratorService.generate(request.getParcelId(), request.getEmail()))
                .email(request.getEmail())
                .build();
    }

    @Override
    public RerouteParcelResponse update(RerouteParcelRequest request) {
        logReroute(request);

        final RerouteParcel parcel = extractParcelFromRequest(request);

        if (!parcel.isRequiredToReroute()) {
            throw new RerouteException(RerouteExceptionCodes.REROUTE_100);
        }

        final RerouteToken rerouteToken = extractTokenFromRequest(request);

        if (!rerouteToken.isValid()) {
            throw new RerouteTokenExpiredException(REROUTE_102);
        }

        prepareToReroute(parcel);

        final Parcel parcelUpdate = parcelReroutePort.reroute(parcel, new ParcelId(request.getId()));

        rerouteService.deleteToken(rerouteToken);

        return RerouteParcelResponse.builder()
                .parcelId(parcelUpdate.getParcelId())
                .parcelSize(parcelUpdate.getParcelSize())
                .parcelType(parcelUpdate.getParcelType())
                .recipient(parcelUpdate.getRecipient())
                .sender(parcelUpdate.getSender())
                .status(parcelUpdate.getStatus())
                .parcelRelatedId(parcelUpdate.getParcelRelatedId())
                .build();
    }

    private void logReroute(RerouteParcelRequest request) {
        logger.info("Detected reroute service for parcel: {} ", request.getId());
    }

    private void prepareToReroute(RerouteParcel parcel) {
        parcel.setStatus(Status.REROUTE);
    }

    private RerouteParcel extractParcelFromRequest(RerouteParcelRequest request) {
        return request.getParcel();
    }

    private RerouteToken extractTokenFromRequest(RerouteParcelRequest request) {
        return rerouteService.loadByTokenAndParcelId(request.getToken(), request.getId());
    }
}
