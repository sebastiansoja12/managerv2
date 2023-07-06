package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteTokenValidatorService;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteService rerouteService;

    private final RerouteTokenValidatorService rerouteTokenValidatorService;

    @Override
    public ParcelUpdateResponse update(UpdateParcelRequest request) {
        validateRequest(request);
        return rerouteService.update(request);
    }

    @Override
    public RerouteToken findByToken(Token token) {
        return rerouteService.findByToken(token);
    }

    @Override
    public RerouteToken loadByTokenAndParcelId(Token token, ParcelId aParcelId) {
        return rerouteService.loadByTokenAndParcelId(token, aParcelId);
    }

    @Override
    public RerouteResponse sendReroutingInformation(RerouteRequest rerouteRequest) {
        return rerouteService.sendReroutingInformation(rerouteRequest);
    }

    public void validateRequest(UpdateParcelRequest request) {
        final boolean rerouteTokenValidate = rerouteTokenValidatorService.validate(request.getToken());
        if (!rerouteTokenValidate) {
            throw new RerouteTokenNotFoundException("Reroute token was not found");
        }
        if (request.getParcel().getParcelType().equals(ParcelType.CHILD)) {
            throw new IllegalArgumentException("Parcel cannot be rerouted after redirection");
        } else if (!request.getParcel().getStatus().equals(Status.CREATED)) {
            throw new IllegalArgumentException("Parcel cannot be rerouted because it was already sent");
        }
    }
}
