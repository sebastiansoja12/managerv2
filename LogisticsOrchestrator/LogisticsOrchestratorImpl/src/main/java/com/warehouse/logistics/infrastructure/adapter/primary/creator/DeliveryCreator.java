package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Request;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Response;
import java.util.Set;

import com.warehouse.commonassets.enumeration.ProcessType;

public interface DeliveryCreator {
    boolean canHandle(final ProcessType processType);
    Set<LogisticsRequest> create(final Request request, final Response response);
}
