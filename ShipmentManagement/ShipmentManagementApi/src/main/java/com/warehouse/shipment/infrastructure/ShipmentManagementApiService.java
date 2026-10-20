package com.warehouse.shipment.infrastructure;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.dto.ShipmentCreateApiRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentCreatedDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyRequestDto;

public interface ShipmentManagementApiService {

    ShipmentCreatedDto createShipment(final ShipmentCreateApiRequestDto request);

    void changeSender(final ShipmentId shipmentId, final ShipmentPartyRequestDto sender);

    void changeRecipient(final ShipmentId shipmentId, final ShipmentPartyRequestDto recipient);
}
