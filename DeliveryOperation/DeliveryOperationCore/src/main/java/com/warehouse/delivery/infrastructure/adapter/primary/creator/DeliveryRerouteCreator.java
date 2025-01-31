package com.warehouse.delivery.infrastructure.adapter.primary.creator;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeliveryRerouteCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return false;
    }

    @Override
    public Set<DeliveryRequest> create(final Request request, final Response response) {
        return Set.of();
    }
}
