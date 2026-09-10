package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.application.port.primary.command.*;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentControlCenterResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.vo.*;

import java.util.List;
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

    ShipmentControlCenterResult loadShipmentControlCenter(final ShipmentId shipmentId);

    ShipmentControlCenterResult loadShipmentControlCenter(final TrackingNumber trackingNumber);

    List<ShipmentResult> searchShipments(final ShipmentSearchCriteria criteria);

    boolean existsShipment(final ShipmentId shipmentId);

    Optional<DangerousGood> loadDangerousGood(final ShipmentId shipmentId);

    void putDangerousGood(final ShipmentId shipmentId, final DangerousGood dangerousGood);

    void deleteDangerousGood(final ShipmentId shipmentId);

    void processShipmentReturn(final ShipmentReturnCommand request);

    void startProcessingShipmentReturn(final ShipmentId shipmentId);

    void completeShipmentReturn(final ShipmentId shipmentId);

    void cancelShipmentReturn(final ReturnId returnId);

    ShipmentReturnDetails loadShipmentReturn(final ReturnId returnId);

    ShipmentReturnPage loadShipmentReturns(final DepartmentCode departmentCode, final int page, final int size);

    void processShipmentDelivery(final ShipmentDeliveryCommand command);

    void cancel(final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ShipmentId shipmentId,
                              final ShipmentType shipmentType,
                              final ShipmentId relatedShipmentId);

    void removeDangerousGood(final ShipmentId shipmentId);

    void lockShipment(final ShipmentId shipmentId);

    void redirectShipmentToSender(final ShipmentId shipmentId);

    void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination);

}
