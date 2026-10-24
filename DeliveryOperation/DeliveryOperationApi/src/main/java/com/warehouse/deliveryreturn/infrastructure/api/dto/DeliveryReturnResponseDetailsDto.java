package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.SupplierCodeDto;
import com.warehouse.commonassets.identificator.TrackingNumber;

public class DeliveryReturnResponseDetailsDto {
    private final ProcessIdDto processId;
    private final ShipmentIdDto shipmentId;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final DeliveryStatusDto deliveryStatus;
    private final ReturnTokenDto returnToken;
    private final UpdateStatusDto updateStatus;
    private final String errorMessage;
    private final TrackingNumber trackingNumber;
    private final ShipmentIdDto newShipmentId;
    private final TrackingNumber newTrackingNumber;


    public DeliveryReturnResponseDetailsDto(final ProcessIdDto processId,
                                            final ShipmentIdDto shipmentId,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final DeliveryStatusDto deliveryStatus,
                                            final ReturnTokenDto returnToken,
                                            final UpdateStatusDto updateStatus,
                                            final String errorMessage,
                                            final TrackingNumber trackingNumber,
                                            final ShipmentIdDto newShipmentId,
                                            final TrackingNumber newTrackingNumber) {
        this.processId = processId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
        this.errorMessage = errorMessage;
        this.trackingNumber = trackingNumber;
        this.newShipmentId = newShipmentId;
        this.newTrackingNumber = newTrackingNumber;
    }

    public ShipmentIdDto getNewShipmentId() {
        return newShipmentId;
    }

    public TrackingNumber getNewTrackingNumber() {
        return newTrackingNumber;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public UpdateStatusDto getUpdateStatus() {
        return updateStatus;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessIdDto getProcessId() {
        return processId;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public ReturnTokenDto getReturnToken() {
        return returnToken;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
