package com.warehouse.logistics.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LogisticsService {
    Optional<Delivery> findById(final DeliveryId deliveryId);

    Set<LogisticsResponse> save(final Set<LogisticsRequest> delivery);

    Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type);

    List<Delivery> findRecent(final int offset, final int limit);

    void createOrUpdate(final Delivery delivery);

    Optional<Delivery> findByShipmentId(final ShipmentId shipmentId);
}
