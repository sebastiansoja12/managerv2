package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Request;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Response;
import com.warehouse.commonassets.enumeration.ProcessType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeliveryRerouteCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return false;
    }

    @Override
    public Set<LogisticsRequest> create(final Request request, final Response response) {
        return Set.of();
    }
}
