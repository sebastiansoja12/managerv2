package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;
import com.warehouse.logistics.domain.port.secondary.RouteLogDeliveryStatusServicePort;
import com.warehouse.logistics.domain.service.LogisticsService;

public class LogisticsPortImpl implements LogisticsPort {

    private final LogisticsService logisticsService;

    private final RouteLogDeliveryStatusServicePort logServicePort;

    public LogisticsPortImpl(final LogisticsService logisticsService,
                             final RouteLogDeliveryStatusServicePort logServicePort) {
        this.logisticsService = logisticsService;
        this.logServicePort = logServicePort;
    }

    @Override
    public Set<DeliveryResponse> processDelivery(final Set<DeliveryRequest> deliveryRequests) {
        return this.logisticsService.save(deliveryRequests);
    }
}
