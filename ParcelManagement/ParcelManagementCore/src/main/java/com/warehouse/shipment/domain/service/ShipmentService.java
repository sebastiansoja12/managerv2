package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

public interface ShipmentService {

    void createShipment(final Shipment shipment);

    Shipment find(final ShipmentId shipmentId);

    void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    void changeSender(final ShipmentId shipmentId, final Sender sender);

    void changeRecipient(final ShipmentId shipmentId, final Recipient recipient);

    void changeShipmentType(final ShipmentId shipmentId, final ShipmentType shipmentType);

    void changeShipmentStatus(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);

    ShipmentId nextShipmentId();
}
