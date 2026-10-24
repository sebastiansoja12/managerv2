package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.application.port.primary.command.*;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentRerouteResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentOwnerContext;

import java.util.Optional;


public interface ShipmentPort {

    Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand request);

    void changePersonTo(final Party party, final PersonType personType, final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ChangeShipmentTypeRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void markDelivered(final ShipmentId shipmentId);

    void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod);

    ShipmentResult loadShipment(final ShipmentId shipmentId);

    ShipmentResult loadShipment(final TrackingNumber trackingNumber);

    ShipmentRouteLog loadShipmentWithRouteLog(final ShipmentId shipmentId);

    ShipmentRouteLog loadShipmentWithRouteLog(final TrackingNumber trackingNumber);

    ShipmentResult collectShipment(final TrackingNumber trackingNumber);

    ShipmentResult handOverShipment(final ShipmentHandoverCommand command);

    ShipmentResult loadShipmentOnPallet(final ShipmentId shipmentId);

    Optional<OperatorId> findOperator(final TrackingNumber trackingNumber);

    Optional<ShipmentResult> findSourceShipment(final ShipmentId shipmentId);

    ShipmentRerouteResult loadShipmentForReroute(final TrackingNumber trackingNumber);

    ShipmentRerouteResult reroute(final ShipmentRerouteCommand command);

    boolean existsShipment(final ShipmentId shipmentId);

    void processShipmentDelivery(final ShipmentDeliveryCommand command);

    void cancel(final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ShipmentId shipmentId,
                              final ShipmentType shipmentType,
                              final ShipmentId relatedShipmentId);

    void lockShipment(final ShipmentId shipmentId);

    void redirectShipmentToSender(final ShipmentId shipmentId);

    void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination);

    void markReturned(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void notifyShipmentReturnCompleted(final ShipmentId shipmentId);

    void notifyShipmentReturnCanceled(final ShipmentId shipmentId);

    void returnToSender(final ShipmentId shipmentId);

    boolean hasAnyShipment();

    ShipmentOwnerContext findContext(final TrackingNumber trackingNumber);
}
