package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.ShipmentCreateRequest;
import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;

public interface ShipmentPort {

    ShipmentCreateResponse ship(final ShipmentCreateRequest request);

    void update(final ShipmentUpdateRequest request);

    void changeSenderTo(final ShipmentCreateRequest request);

    void changeRecipientTo(final ShipmentCreateRequest request);

    void changeShipmentTypeTo(final ShipmentCreateRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request);

    Shipment loadShipment(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

}
