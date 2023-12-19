package com.warehouse.parcelstatuschange.domain.port.primary;

import java.util.Objects;

import com.warehouse.parcelstatuschange.domain.exception.ParcelRequestEmptyException;
import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import com.warehouse.parcelstatuschange.domain.vo.Parcel;
import com.warehouse.parcelstatuschange.domain.vo.StatusRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ParcelStatusPortImpl implements ParcelStatusPort {

    private final ParcelStatusRepository parcelStatusRepository;

    @Override
    public void updateStatus(@NotNull StatusRequest statusRequest) {
        final Parcel parcel = statusRequest.getParcel();
        validateParcel(parcel);
        logStatusRequest(parcel);
        parcelStatusRepository.update(parcel);
    }

    private void validateParcel(Parcel parcel) {
        if (Objects.isNull(parcel)) {
            throw new ParcelRequestEmptyException(404, "Parcel is null");
        }
    }

    private void logStatusRequest(Parcel parcel) {
        log.warn("Update status of parcel {}, trying to set status {}", parcel.getId(), parcel.getParcelStatus());
    }
}
