package com.warehouse.delivery.infrastructure.adapter.primary.creator;

import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;

@Component
public class DeliveryReturnCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return RETURN.equals(processType);
    }

    @Override
    public Set<DeliveryRequest> create(final Request request, final Response response) {
        return null;
    }
}