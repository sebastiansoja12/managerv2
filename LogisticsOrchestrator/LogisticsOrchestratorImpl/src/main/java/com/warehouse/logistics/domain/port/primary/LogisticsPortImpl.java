package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.service.LogisticsService;

public class LogisticsPortImpl implements LogisticsPort {

    private final LogisticsService logisticsService;

    public LogisticsPortImpl(final LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @Override
    public Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequests) {
        return this.logisticsService.save(logisticsRequests);
    }
}
