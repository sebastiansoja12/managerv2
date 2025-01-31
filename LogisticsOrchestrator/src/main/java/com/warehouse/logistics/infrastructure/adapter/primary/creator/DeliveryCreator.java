package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import java.util.Set;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;

public interface DeliveryCreator {
    boolean canHandle(final ProcessType processType);
    Set<DeliveryRequest> create(final Request request, final Response response);
}
