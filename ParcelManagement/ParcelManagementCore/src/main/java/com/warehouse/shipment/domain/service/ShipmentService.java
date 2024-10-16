package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentPriority;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.Sender;

public interface ShipmentService {

    void createShipment(final Shipment shipment);

    ShipmentId createCopy(final ShipmentId shipmentId);

    void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId);

    void changeSenderTo(final ShipmentId shipmentId, final Sender sender);

    void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient);

    void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType);

    void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);

    void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority);

    void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency);

    void changeShipmentOriginCountryTo(final ShipmentId shipmentId, final Country originCountry);

    void changeShipmentDestinationCountryTo(final ShipmentId shipmentId, final Country destinationCountry);

    void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired);

    void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void notifyRelatedShipmentLocked(final ShipmentId shipmentId);

    Shipment find(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    RouteProcess initializeRouteProcess(final ShipmentId shipmentId);

    ShipmentId nextShipmentId();
}
