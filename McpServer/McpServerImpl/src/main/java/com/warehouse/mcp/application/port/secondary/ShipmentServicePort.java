package com.warehouse.mcp.application.port.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.domain.vo.ShipmentTracking;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mcp.domain.vo.PartyRole;
import com.warehouse.mcp.domain.vo.ShipmentDraft;
import com.warehouse.mcp.domain.vo.ShipmentParty;

public interface ShipmentServicePort {
    ShipmentTracking findShipmentTracking(final TrackingNumber trackingNumber);
    TrackingNumber createShipment(final ShipmentDraft shipment);
    void changeParty(final ShipmentId shipmentId, final PartyRole role, final ShipmentParty party);
}
