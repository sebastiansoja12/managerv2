package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountryRequest;

public interface ShipmentService {

    void createShipment(final Shipment shipment);

    void changeSenderTo(final ShipmentId shipmentId, final Sender sender);

    void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient);

    void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType, final ShipmentId relatedShipmentId);

    void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);

    void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority);

    void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency);

    void changeShipmentIssuerCountryTo(final ShipmentId shipmentId, final Country originCountry);

    void changeShipmentReceiverCountryTo(final ShipmentId shipmentId, final Country destinationCountry);

    void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired);

    void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void notifyShipmentRerouted(final ShipmentId shipmentId);

    void notifyRelatedShipmentLocked(final ShipmentId shipmentId);

    void notifyShipmentSent(final ShipmentId shipmentId);

    void notifyShipmentReturned(final ShipmentId shipmentId);

    void notifyShipmentDelivered(final ShipmentId shipmentId);

    void changeShipmentCountries(ShipmentCountryRequest request);

    void lockShipment(final ShipmentId shipmentId);

    Shipment find(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    ShipmentId nextShipmentId();

}
