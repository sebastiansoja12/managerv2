package com.warehouse.parcelstatuschange.domain.port.primary;

import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.domain.vo.StatusRequest;

public interface ParcelStatusPort {

    void updateStatus(StatusRequest statusRequest);

    Status getStatus(Long parcelId);
}
