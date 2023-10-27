package com.warehouse.parcelstatuschange.domain.port.secondary;

import com.warehouse.parcelstatuschange.domain.model.StatusRequest;

public interface ParcelStatusRepository {
    void update(StatusRequest statusRequest);
}
