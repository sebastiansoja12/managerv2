package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;

import java.util.Optional;

public interface DeliveryNetworkRepository {

    Optional<DeliveryNetwork> find();

    void save(final DeliveryNetwork deliveryNetwork);
}
