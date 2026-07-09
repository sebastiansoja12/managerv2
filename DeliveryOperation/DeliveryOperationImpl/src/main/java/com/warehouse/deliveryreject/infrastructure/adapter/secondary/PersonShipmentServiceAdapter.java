package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreject.domain.port.secondary.PersonShipmentServicePort;
import com.warehouse.deliveryreject.domain.vo.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonShipmentServiceAdapter implements PersonShipmentServicePort {
    @Override
    public Person getRecipient(final ShipmentId shipmentId) {
        return null;
    }
}
