package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.enumeration.Status;

public interface ParcelStatusUpdateRepository {
    void updateStatus(Long parcelId, Status status);
}
