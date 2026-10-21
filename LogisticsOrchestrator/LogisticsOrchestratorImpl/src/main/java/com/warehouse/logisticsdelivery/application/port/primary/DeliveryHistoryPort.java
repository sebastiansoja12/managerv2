package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;

import java.util.List;

public interface DeliveryHistoryPort {

    void record(final DeliveryChanged event);

    List<DeliveryHistoryEntry> findHistory(final DeliveryId deliveryId);
}
