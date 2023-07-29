package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteUpdatePortImpl implements RerouteUpdatePort {

    private final RerouteService rerouteService;

    private final ParcelReroutePort parcelReroutePort;

    @Override
    public RerouteParcelResponse update(RerouteParcelRequest request) {
        final RerouteParcel parcel = extractParcelFromRequest(request);

        if (!parcel.isRequiredToReroute()) {
            throw new RerouteException("Parcel cannot be rerouted");
        }

        final RerouteToken rerouteToken = extractTokenFromRequest(request);

        if (!rerouteToken.isValid()) {
            throw new RerouteTokenNotFoundException("Reroute token is not valid");
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
