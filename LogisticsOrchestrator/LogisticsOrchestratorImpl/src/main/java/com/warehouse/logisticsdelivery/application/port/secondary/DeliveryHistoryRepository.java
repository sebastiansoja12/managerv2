package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;

import java.util.List;

public interface DeliveryHistoryRepository {

    void save(final DeliveryHistoryEntry entry);

    List<DeliveryHistoryEntry> findByDeliveryId(final DeliveryId deliveryId);
}
