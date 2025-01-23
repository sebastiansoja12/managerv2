package com.warehouse.delivery.infrastructure.adapter.primary.creator;

import java.util.Set;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;

public interface DeliveryCreator {
    boolean canHandle(final ProcessType processType);
    Set<DeliveryRequest> create(final Request request, final Response response);
}
