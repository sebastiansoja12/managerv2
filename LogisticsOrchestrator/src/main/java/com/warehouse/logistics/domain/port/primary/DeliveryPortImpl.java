package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;
import com.warehouse.logistics.domain.port.secondary.RouteLogDeliveryStatusServicePort;
import com.warehouse.logistics.domain.service.DeliveryService;

public class DeliveryPortImpl implements DeliveryPort {

    private final DeliveryService deliveryService;

    private final RouteLogDeliveryStatusServicePort logServicePort;

    public DeliveryPortImpl(final DeliveryService deliveryService,
                            final RouteLogDeliveryStatusServicePort logServicePort) {
        this.deliveryService = deliveryService;
        this.logServicePort = logServicePort;
    }

    @Override
    public Set<DeliveryResponse> processDelivery(final Set<DeliveryRequest> deliveryRequests) {
        return this.deliveryService.save(deliveryRequests);
    }
}
