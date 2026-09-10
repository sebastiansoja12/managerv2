package com.warehouse.logistics.application.port.primary;


import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import java.util.Set;

public interface LogisticsPort {

    Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequest);
}
