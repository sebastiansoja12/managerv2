package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
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
    public Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequests) {
        return this.logisticsService.save(logisticsRequests);
    }
}
