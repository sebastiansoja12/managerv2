package com.warehouse.parcelstate.domain.port.primary;

import com.warehouse.parcelstate.domain.model.DeliveryStateRequest;
import com.warehouse.parcelstate.domain.model.DeliveryStateResponse;

public interface ParcelStatePort {

    DeliveryStateResponse updateStatus(DeliveryStateRequest request);
}
