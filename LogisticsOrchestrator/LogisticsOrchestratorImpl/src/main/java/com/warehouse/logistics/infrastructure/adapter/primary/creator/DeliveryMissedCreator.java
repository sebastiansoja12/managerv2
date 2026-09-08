package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Request;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Response;
import static com.warehouse.commonassets.enumeration.ProcessType.MISS;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;

@Component
public class DeliveryMissedCreator implements DeliveryCreator {


    @Override
    public boolean canHandle(final ProcessType processType) {
        return MISS.equals(processType);
    }

    @Override
    public Set<LogisticsRequest> create(final Request request, final Response response) {
        return null;
    }
}
