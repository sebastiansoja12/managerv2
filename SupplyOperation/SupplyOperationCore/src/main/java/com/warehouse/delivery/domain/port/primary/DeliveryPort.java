package com.warehouse.delivery.domain.port.primary;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

import java.util.List;

public interface DeliveryPort {

    List<DeliveryResponse> deliver(List<DeliveryRequest> deliveryRequest);
}
