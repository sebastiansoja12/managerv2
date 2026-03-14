package com.warehouse.deliveryreject.domain.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;

public interface RejectShipmentServicePort {
    List<ShipmentRejectResponse> notifyShipmentRejection(final List<ShipmentRejectRequest> requests,
                                                         final ProcessId processId);
}
