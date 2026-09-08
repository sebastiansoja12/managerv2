package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

public interface DeliveryServicePort {

    LogisticsResponse recordAttempt(final LogisticsRequest request, final UserId userId, final DepartmentId departmentId);
}
