package com.warehouse.delivery.infrastructure.adapter.primary.creator;

import static com.warehouse.commonassets.enumeration.ProcessType.REJECT;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;

@Component
public class DeliveryRejectCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return REJECT.equals(processType);
    }

    @Override
    public Set<DeliveryRequest> create(final Request request, final Response response) {
        return Collections.emptySet();
    }
}
