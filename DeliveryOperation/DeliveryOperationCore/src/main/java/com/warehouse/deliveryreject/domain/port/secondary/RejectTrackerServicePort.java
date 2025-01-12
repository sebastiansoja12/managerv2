package com.warehouse.deliveryreject.domain.port.secondary;

import com.warehouse.deliveryreject.domain.vo.RejectTrackerRequest;
import com.warehouse.deliveryreject.domain.vo.RejectTrackerResponse;

public interface RejectTrackerServicePort {
    RejectTrackerResponse logRejectInTracker(final RejectTrackerRequest rejectTrackerRequest);
}
