package com.warehouse.reroute.domain.port.primary;

import static com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes.REROUTE_102;

import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.Logger;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteTokenGeneratorService;
import com.warehouse.reroute.domain.vo.GeneratedToken;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;

import com.warehouse.reroute.domain.vo.RerouteResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteService rerouteService;

    private final ParcelReroutePort parcelReroutePort;

    private final RerouteTokenGeneratorService tokenGeneratorService;

    private final Logger logger;


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
		final GeneratedToken generatedToken = new GeneratedToken(
                tokenGeneratorService.generate(rerouteRequest.getParcelId(), rerouteRequest.getEmail()));
        return rerouteService.createRerouteToken(rerouteRequest, generatedToken);
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
                .status(parcelUpdate.getParcelStatus())
                .parcelRelatedId(parcelUpdate.getParcelRelatedId())
                .build();
    }

    private void logReroute(RerouteParcelRequest request) {
        logger.info("Detected reroute service for parcel: {} ", request.getId());
    }

    private void prepareToReroute(RerouteParcel parcel) {
        parcel.setParcelStatus(Status.REROUTE);
    }

    private RerouteParcel extractParcelFromRequest(RerouteParcelRequest request) {
        return request.getParcel();
    }

    private RerouteToken extractTokenFromRequest(RerouteParcelRequest request) {
        return rerouteService.loadByTokenAndParcelId(request.getToken(), request.getId());
    }
}
