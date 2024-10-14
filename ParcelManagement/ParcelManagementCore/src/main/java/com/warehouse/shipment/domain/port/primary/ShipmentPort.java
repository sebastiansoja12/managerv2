package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;

public interface ShipmentPort {

    ShipmentResponse ship(final ShipmentRequest request);

    void update(final ShipmentUpdateRequest request);

    void changeSender(final ShipmentRequest request);

    void changeRecipient(final ShipmentRequest request);

    void changeShipmentType(final ShipmentRequest request);

    void changeShipmentStatus(final ShipmentStatusRequest request);

    Shipment loadParcel(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);
}
