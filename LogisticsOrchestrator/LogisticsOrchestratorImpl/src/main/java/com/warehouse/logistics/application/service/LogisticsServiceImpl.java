package com.warehouse.logistics.application.service;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.DepartmentServicePort;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;

import java.util.Set;
import java.util.stream.Collectors;

public class LogisticsServiceImpl implements LogisticsService {

    private final DeliveryServicePort deliveryServicePort;
    private final DepartmentServicePort departmentServicePort;
    private final OperatorContextProvider operatorContextProvider;

    public LogisticsServiceImpl(final DeliveryServicePort deliveryServicePort,
                                final DepartmentServicePort departmentServicePort,
                                final OperatorContextProvider operatorContextProvider) {
        this.deliveryServicePort = deliveryServicePort;
        this.departmentServicePort = departmentServicePort;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public Set<LogisticsResponse> save(final Set<LogisticsRequest> logisticsRequests) {
        return logisticsRequests.stream()
                .map(this::process)
                .collect(Collectors.toSet());
    }

    private LogisticsResponse process(final LogisticsRequest request) {
        final UserId userId = operatorContextProvider.currentUserId();
        final DepartmentId departmentId = departmentServicePort.findIdByCode(request.getDepartmentCode());
        return deliveryServicePort.recordAttempt(request, userId, departmentId);
    }
}
