package com.warehouse.deliverycollect.application.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverycollect.domain.vo.CollectResult;

public interface ShipmentCollectServicePort {
    CollectResult collect(final ProcessId processId, final TrackingNumber trackingNumber);
}
