package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteTokenValidatorService;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteService rerouteService;

    private final RerouteTokenValidatorService rerouteTokenValidatorService;

    private final RerouteTokenRepository rerouteTokenRepository;

    @Override
    public ParcelUpdateResponse update(UpdateParcelRequest request) {
        final Parcel parcel = extractParcelFromRequest(request);

        if (!parcel.isRequiredToReroute()) {
            throw new RerouteException("Parcel cannot be rerouted");
        }

        final RerouteToken rerouteToken = extractTokenFromRequest(request);

        if (rerouteToken.isValid()) {
            throw new RerouteTokenNotFoundException("Reroute token is not valid");
        }

        parcel.setStatus(Status.REROUTE);

        return rerouteService.update(parcel, rerouteToken);
    }

    private RerouteToken extractTokenFromRequest(UpdateParcelRequest request) {
        return rerouteTokenRepository.loadByTokenAndParcelId(request.getToken(), request.getId());
    }

    private Parcel extractParcelFromRequest(UpdateParcelRequest request) {
        return request.getParcel();
    }

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
        return rerouteService.sendReroutingInformation(rerouteRequest);
    }
}
