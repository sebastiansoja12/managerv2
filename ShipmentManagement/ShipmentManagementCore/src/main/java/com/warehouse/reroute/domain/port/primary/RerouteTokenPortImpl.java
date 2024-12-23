package com.warehouse.reroute.domain.port.primary;

import static com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes.REROUTE_403;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.Logger;
import com.warehouse.reroute.domain.port.secondary.RerouteTrackerServicePort;
import com.warehouse.reroute.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteTokenGeneratorService;
import com.warehouse.reroute.domain.vo.GeneratedToken;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.domain.vo.RerouteResponse;

import com.warehouse.reroute.domain.vo.SoftwareConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteService rerouteService;

    private final RerouteTokenGeneratorService tokenGeneratorService;

    private final Logger logger;

    private final RerouteTrackerServicePort rerouteTrackerServicePort;
    
    private final SoftwareConfigurationServicePort softwareConfigurationServicePort;

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
            throw new RerouteException(RerouteExceptionCodes.REROUTE_403_EMPTY_PARCEL);
        }

        final RerouteToken rerouteToken = extractTokenFromRequest(request);

        if (!rerouteToken.isValid()) {
            throw new RerouteTokenExpiredException(REROUTE_403);
        }

        prepareToReroute(parcel);

        rerouteService.deleteToken(rerouteToken);

        return RerouteParcelResponse.builder().build();
    }

    @Override
    public void invalidateToken(final ShipmentId shipmentId) {
        this.rerouteService.invalidateToken(shipmentId);
		final SoftwareConfiguration softwareConfiguration = this.softwareConfigurationServicePort
				.getSoftwareConfiguration();
        this.rerouteTrackerServicePort.sendRerouteRequest(softwareConfiguration, shipmentId);
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
