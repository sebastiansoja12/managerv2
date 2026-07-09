package com.warehouse.logistics.domain.service;

import java.util.Set;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;

public interface LogisticsService {
    Set<LogisticsResponse> save(final Set<LogisticsRequest> delivery);
}
