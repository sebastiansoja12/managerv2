package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import org.springframework.stereotype.Component;

@Component
public class ShipmentActorServiceAdapter implements ShipmentActorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public ShipmentActorServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public UserId currentUserId() {
        return operatorContextProvider.currentUserId();
    }
}
