package com.warehouse.csv.domain.port.secondary;

import com.warehouse.csv.domain.vo.ParcelCsv;

public interface ShipmentServicePort {
    ParcelCsv find(final Long shipmentId);
}
