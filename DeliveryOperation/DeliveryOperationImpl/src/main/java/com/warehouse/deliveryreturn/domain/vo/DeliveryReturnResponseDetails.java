package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;

import lombok.Builder;
import com.warehouse.commonassets.identificator.TrackingNumber;


@Builder
public class DeliveryReturnResponseDetails {
    private ProcessId processId;
    private DeliveryId deliveryId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private ShipmentId shipmentId;
    private DeliveryStatus deliveryStatus;
    private ReturnToken returnToken;
    private UpdateStatus updateStatus;
    private String errorMessage;
    private TrackingNumber trackingNumber;
    private ShipmentId newShipmentId;
    private TrackingNumber newTrackingNumber;

    public DeliveryReturnResponseDetails(final ProcessId processId,
                                         final DeliveryId deliveryId,
                                         final DepartmentCode departmentCode,
                                         final SupplierCode supplierCode,
                                         final ShipmentId shipmentId,
                                         final DeliveryStatus deliveryStatus,
                                         final ReturnToken returnToken,
                                         final UpdateStatus updateStatus,
                                         final String errorMessage,
                                         final TrackingNumber trackingNumber,
                                         final ShipmentId newShipmentId,
                                         final TrackingNumber newTrackingNumber) {
        this.processId = processId;
        this.deliveryId = deliveryId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
        this.errorMessage = errorMessage;
        this.trackingNumber = trackingNumber;
        this.newShipmentId = newShipmentId;
        this.newTrackingNumber = newTrackingNumber;
    }

    public static DeliveryReturnResponseDetails from(final DeliveryReturn deliveryReturn,
                                                     final ShipmentReturnResult returnResult,
                                                     final TrackingNumber trackingNumber) {
        final ProcessId id = deliveryReturn.getProcessId() == null ? null : new ProcessId(deliveryReturn.getProcessId());
        final ShipmentId shipmentIdentificator = new ShipmentId(deliveryReturn.getShipmentId());
        final DeliveryStatus status = DeliveryStatus.valueOf(deliveryReturn.getDeliveryStatus());
        final ReturnToken token = new ReturnToken(deliveryReturn.getToken());
        final DepartmentCode departmentCode = new DepartmentCode(deliveryReturn.getDepartmentCode());
        final SupplierCode supplierCode = new SupplierCode(deliveryReturn.getSupplierCode());
        return new DeliveryReturnResponseDetails(id, null, departmentCode, supplierCode,
                shipmentIdentificator, status, token, returnResult.updateStatus(), returnResult.errorMessage(),
                trackingNumber, returnResult.newShipmentId(), returnResult.newTrackingNumber());
    }

    public static DeliveryReturnResponseDetails wrongReturnToken(final ProcessId processId,
                                                                 final ShipmentId shipmentId,
                                                                 final DepartmentCode departmentCode,
                                                                 final SupplierCode supplierCode,
                                                                 final DeliveryStatus deliveryStatus,
                                                                 final ReturnToken returnToken,
                                                                 final String message,
                                                                 final TrackingNumber trackingNumber) {
        return new DeliveryReturnResponseDetails(
                processId,
                null,
                departmentCode,
                supplierCode,
                shipmentId,
                deliveryStatus,
                returnToken,
                UpdateStatus.NOT_OK,
                message,
                trackingNumber,
                null,
                null
        );
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public TrackingNumber getNewTrackingNumber() {
        return newTrackingNumber;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
