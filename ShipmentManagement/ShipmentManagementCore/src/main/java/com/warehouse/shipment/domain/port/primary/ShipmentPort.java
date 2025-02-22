package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;

public interface ShipmentPort {

    ShipmentResponse ship(final ShipmentRequest request);

    void update(final ShipmentUpdateRequest request);

    void changeSenderTo(final ShipmentRequest request);

    void changeRecipientTo(final ShipmentRequest request);

    void changeShipmentTypeTo(final ShipmentRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request);

    Shipment loadShipment(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

}
