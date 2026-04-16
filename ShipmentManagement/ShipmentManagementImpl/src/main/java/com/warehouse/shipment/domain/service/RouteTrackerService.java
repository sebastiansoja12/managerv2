package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Person;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.Sender;

public interface RouteTrackerService {
    Result<RouteProcess, ErrorCode> notifyShipmentCreated(final ShipmentId shipmentId);
    Result<RouteProcess, ErrorCode> notifyShipmentRecipientChanged(final ShipmentId shipmentId, final Recipient recipient);
    Result<RouteProcess, ErrorCode> notifyShipmentPersonChanged(final ShipmentId shipmentId, final Person person);
    Result<RouteProcess, ErrorCode> notifyShipmentSenderChanged(final ShipmentId shipmentId, final Sender sender);
    Result<RouteProcess, ErrorCode> notifyShipmentStatusChanged(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);
    Result<RouteProcess, ErrorCode> notifyShipmentSent(final ShipmentId shipmentId);
    Result<RouteProcess, ErrorCode> notifyShipmentDelivered(final ShipmentId shipmentId);
    Result<RouteProcess, ErrorCode> notifyShipmentLocked(final ShipmentId shipmentId);
    Result<RouteProcess, ErrorCode> notifyShipmentCurrencyChanged(final ShipmentId shipmentId, final Currency currency);
}
