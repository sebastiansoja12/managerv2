package com.warehouse.deliveryhandover.application.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;
import com.warehouse.deliveryhandover.domain.vo.RecipientConfirmation;

public interface ShipmentHandoverServicePort {
    HandoverResult handOver(final ProcessId processId,
                            final TrackingNumber trackingNumber,
                            final RecipientConfirmation recipientConfirmation);
}
