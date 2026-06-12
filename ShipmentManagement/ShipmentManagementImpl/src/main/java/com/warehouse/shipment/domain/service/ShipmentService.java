package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountryRequest;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {

    void createShipment(final Shipment shipment);

    void changeSenderTo(final ShipmentId shipmentId, final Sender sender);

    void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient);

    void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType, final ShipmentId relatedShipmentId);

    void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);

    void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority);

    void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency);

    void changeShipmentIssuerCountryTo(final ShipmentId shipmentId, final CountryCode originCountry);

    void changeShipmentReceiverCountryTo(final ShipmentId shipmentId, final CountryCode destinationCountry);

    void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired);

    void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    Optional<DangerousGood> findDangerousGood(final ShipmentId shipmentId);

    void removeDangerousGood(final ShipmentId shipmentId);

    void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId);

    void notifyShipmentRerouted(final ShipmentId shipmentId);

    void notifyRelatedShipmentLocked(final ShipmentId shipmentId);

    void notifyShipmentSent(final ShipmentId shipmentId);

    default void notifyShipmentReturned(final ShipmentId shipmentId, final String reason, final ReasonCode reasonCode,
                                        final DepartmentCode departmentCode) {
        notifyShipmentReturned(shipmentId);
    }

    void notifyShipmentReturned(final ShipmentId shipmentId);

    void notifyShipmentDelivered(final ShipmentId shipmentId);

    void notifyReturnCanceled(final ShipmentId shipmentId);

    void changeShipmentCountries(ShipmentCountryRequest request);

    void lockShipment(final ShipmentId shipmentId);

    Shipment find(final ShipmentId shipmentId);

    Shipment find(final TrackingNumber trackingNumber);

    List<Shipment> search(final ShipmentSearchCriteria criteria);

    boolean existsShipment(final ShipmentId shipmentId);

    ShipmentId nextShipmentId();

    void update(final Shipment shipment);

    void redirectShipmentToSender(final ShipmentId shipmentId);

    void changeDestination(final ShipmentId shipmentId, final DepartmentCode value);

    Shipment findByExternalId(final ExternalId<String> externalId);
}
