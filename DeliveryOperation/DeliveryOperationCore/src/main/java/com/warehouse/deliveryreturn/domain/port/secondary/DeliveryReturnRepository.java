package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;

public interface DeliveryReturnRepository {
    DeliveryReturn saveDeliveryReturn(final DeliveryReturnDetails deliveryReturnRequest);
}
