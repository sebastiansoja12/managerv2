package com.warehouse.logistics.application.service;


import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import java.util.Set;

public interface LogisticsService {

    Set<LogisticsResponse> save(final Set<LogisticsRequest> delivery);
}
