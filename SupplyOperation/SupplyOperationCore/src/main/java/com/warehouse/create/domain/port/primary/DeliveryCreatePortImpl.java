package com.warehouse.create.domain.port.primary;

import com.warehouse.create.domain.model.TerminalRequest;
import com.warehouse.create.domain.model.TerminalResponse;
import com.warehouse.create.domain.service.DeliveryCreateService;
import lombok.AllArgsConstructor;

import static com.warehouse.create.domain.enumeration.ProcessType.CREATED;

@AllArgsConstructor
public class DeliveryCreatePortImpl implements DeliveryCreatePort {

    private final DeliveryCreateService deliveryCreateService;


    @Override
    public TerminalResponse createDelivery(TerminalRequest request) {
        if (!CREATED.equals(request.getProcessType())) {
            throw new IllegalArgumentException("Wrong process type");
        }
        return deliveryCreateService.createDelivery(request);
    }
}
