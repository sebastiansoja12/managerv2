package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.application.port.primary.command.*;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.vo.*;

import java.util.List;
import java.util.Optional;

public interface ShipmentPort {

    Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand request);

    Result<Void, ErrorCode> update(final ShipmentUpdateCommand request);

    void changeSenderTo(final ShipmentId shipmentId, final Sender sender);

    void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient);

    void changePersonTo(final Person person, final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ChangeShipmentTypeRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod);

    Shipment loadShipment(final ShipmentId shipmentId);

    Shipment loadShipment(final TrackingNumber trackingNumber);

    ShipmentRouteLog getShipmentByShipmentId(final ShipmentId shipmentId);

    ShipmentRouteLog getShipmenyByTrackingNumber(final TrackingNumber trackingNumber);

    List<Shipment> searchShipments(final ShipmentSearchCriteria criteria);

    boolean existsShipment(final ShipmentId shipmentId);

    Optional<DangerousGood> loadDangerousGood(final ShipmentId shipmentId);

    void putDangerousGood(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    void deleteDangerousGood(final ShipmentId shipmentId);

    void processShipmentReturn(final ShipmentReturnCommand request);

    void cancelShipmentReturn(final ReturnId returnId);

    ShipmentReturnDetails loadShipmentReturn(final ReturnId returnId);

    ShipmentReturnPage loadShipmentReturns(final DepartmentCode departmentCode, final int page, final int size);

    void processShipmentDelivery(final ShipmentDeliveryCommand command);

    void cancel(final ShipmentId shipmentId);

    void createShipment(final Shipment shipment);

    Shipment find(final ShipmentId shipmentId);

    Shipment find(final TrackingNumber trackingNumber);

    List<Shipment> search(final ShipmentSearchCriteria criteria);

    void changeShipmentTypeTo(final ShipmentId shipmentId,
                              final ShipmentType shipmentType,
                              final ShipmentId relatedShipmentId);

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

    void notifyShipmentReturned(final ShipmentId shipmentId);

    void notifyShipmentReturned(final ShipmentId shipmentId,
                                final String reason,
                                final ReasonCode reasonCode,
                                final DepartmentCode departmentCode);

    void notifyShipmentDelivered(final ShipmentId shipmentId);

    void notifyReturnCanceled(final ShipmentId shipmentId);

    void changeShipmentCountries(final ShipmentCountryRequest request);

    void lockShipment(final ShipmentId shipmentId);

    void redirectShipmentToSender(final ShipmentId shipmentId);

    void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination);

    Shipment findByExternalId(final ExternalId<String> externalId);
}
