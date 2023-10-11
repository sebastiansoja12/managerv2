package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.port.secondary.ParcelStateServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStateAdapter implements ParcelStateServicePort {
    @Override
    public void updateParcelStatus(Delivery delivery) {
        // TODO INPL-3713
    }
}
