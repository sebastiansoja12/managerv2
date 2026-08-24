package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ShipmentUpdateCommand;
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
}
