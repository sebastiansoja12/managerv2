package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;

public interface LogisticsPort {

    Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequest);
}
