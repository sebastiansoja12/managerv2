package com.warehouse.parcelstate.domain.port.primary;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.domain.model.RerouteResponse;
import com.warehouse.parcelstate.domain.model.Token;
import com.warehouse.parcelstate.domain.port.secondary.ParcelStateRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStatePortImpl implements ParcelStatePort {

    private final ParcelStateRepository parcelStateRepository;

    @Override
    public RerouteResponse rerouteParcel(RerouteParcel rerouteParcel) {
        final Token token = extractTokenFromRerouteParcel(rerouteParcel);
        final Parcel parcel = extractParcelFromRerouteParcel(rerouteParcel);
        final Parcel updatedParcel = parcelStateRepository.update(parcel);
        return new RerouteResponse(token.getValue(), updatedParcel);
    }

    private Parcel extractParcelFromRerouteParcel(RerouteParcel rerouteParcel) {
        return rerouteParcel.getParcel();
    }

    private Token extractTokenFromRerouteParcel(RerouteParcel parcel) {
        return parcel.getToken();
    }

    private RerouteParcel buildRerouteParcel(Parcel parcel) {
        return new RerouteParcel();
    }
}
