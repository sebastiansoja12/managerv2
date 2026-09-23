package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.application.port.primary.command.*;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.vo.Person;

import java.util.Optional;

public interface ShipmentPort {

    Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand request);

    Result<Void, ErrorCode> update(final ShipmentUpdateCommand request);

    void changePersonTo(final Person person, final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ChangeShipmentTypeRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod);

    ShipmentResult loadShipment(final ShipmentId shipmentId);

    ShipmentResult loadShipment(final TrackingNumber trackingNumber);

    ShipmentRouteLog loadShipmentWithRouteLog(final ShipmentId shipmentId);

    ShipmentRouteLog loadShipmentWithRouteLog(final TrackingNumber trackingNumber);


    boolean existsShipment(final ShipmentId shipmentId);

    Optional<DangerousGood> loadDangerousGood(final ShipmentId shipmentId);

    void putDangerousGood(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    void deleteDangerousGood(final ShipmentId shipmentId);

    void processShipmentDelivery(final ShipmentDeliveryCommand command);

    void cancel(final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ShipmentId shipmentId,
                              final ShipmentType shipmentType,
                              final ShipmentId relatedShipmentId);

    void removeDangerousGood(final ShipmentId shipmentId);

    void lockShipment(final ShipmentId shipmentId);

    void redirectShipmentToSender(final ShipmentId shipmentId);

    void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination);

    void markReturned(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void notifyShipmentReturnCompleted(final ShipmentId shipmentId);

    void notifyShipmentReturnCanceled(final ShipmentId shipmentId);

    void returnToSender(final ShipmentId shipmentId);
}
