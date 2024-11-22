package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;

public interface ShipmentStatusControlServicePort {
    UpdateStatus updateStatus(UpdateStatusParcelRequest updateStatusParcelRequest);
}
