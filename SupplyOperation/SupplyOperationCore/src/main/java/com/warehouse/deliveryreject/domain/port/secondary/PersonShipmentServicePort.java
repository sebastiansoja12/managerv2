package com.warehouse.deliveryreject.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreject.domain.vo.Person;

public interface PersonShipmentServicePort {
    Person getRecipient(final ShipmentId shipmentId);
}
