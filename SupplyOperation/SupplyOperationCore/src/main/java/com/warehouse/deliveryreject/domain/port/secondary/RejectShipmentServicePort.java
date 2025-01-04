package com.warehouse.deliveryreject.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.vo.Person;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;

public interface RejectShipmentServicePort {
    ShipmentRejectResponse notifyShipmentRejection(final ShipmentRejectRequest request);
    Person getRecipient(final ShipmentId shipmentId);
}
