package com.warehouse.parcelstatuschange.domain.port.primary;

import com.warehouse.parcelstatuschange.domain.model.StatusRequest;

public interface ParcelStatusPort {
    void updateStatus(StatusRequest statusRequest);
}
