package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;

public interface ParcelStatusControlChangeServicePort {
    UpdateStatus updateParcelStatus(UpdateStatusParcelRequest request);
}
