package com.warehouse.create.domain.port.primary;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;
import com.warehouse.create.domain.service.DeliveryCreateService;
import lombok.AllArgsConstructor;

import static com.warehouse.create.domain.enumeration.ProcessType.CREATED;

@AllArgsConstructor
public class DeliveryCreatePortImpl implements DeliveryCreatePort {

    private final DeliveryCreateService deliveryCreateService;


    @Override
    public Response createDelivery(Request request) {
        if (!CREATED.equals(request.getProcessType())) {
            throw new IllegalArgumentException("Wrong process type");
        }
        return deliveryCreateService.createDelivery(request);
    }
}
