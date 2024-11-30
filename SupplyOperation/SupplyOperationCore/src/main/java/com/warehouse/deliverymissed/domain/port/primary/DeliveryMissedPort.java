package com.warehouse.deliverymissed.domain.port.primary;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

public interface DeliveryMissedPort {
    DeliveryMissedResponse process(final DeliveryMissedRequest request);
}
