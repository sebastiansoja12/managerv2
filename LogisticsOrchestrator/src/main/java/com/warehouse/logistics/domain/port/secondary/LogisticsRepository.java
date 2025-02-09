package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;

public interface LogisticsRepository {
    LogisticsResponse create(final LogisticsRequest request);
}
