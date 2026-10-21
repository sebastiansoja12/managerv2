package com.warehouse.logistics.application.port.primary;


import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.logistics.application.service.LogisticsService;
import java.util.Set;

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
