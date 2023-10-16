package com.warehouse.parcelstatuschange.domain.port.primary;

import com.warehouse.parcelstatuschange.domain.model.StatusRequest;
import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ParcelStatusPortImpl implements ParcelStatusPort {

    private final ParcelStatusRepository parcelStatusRepository;

    @Override
    public void updateStatus(StatusRequest statusRequest) {
        logStatusRequest(statusRequest);
        parcelStatusRepository.update(statusRequest);
    }

    private void logStatusRequest(StatusRequest statusRequest) {
        log.warn("Update status of parcel {}, setting status {}", statusRequest.getParcel().getId(),
                statusRequest.getParcel().getStatus());
    }
}
