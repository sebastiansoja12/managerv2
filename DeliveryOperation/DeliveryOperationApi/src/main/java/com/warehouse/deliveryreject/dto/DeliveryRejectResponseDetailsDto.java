package com.warehouse.deliveryreject.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;
import com.warehouse.commonassets.identificator.TrackingNumber;

public class DeliveryRejectResponseDetailsDto {
    private final ShipmentIdDto shipmentId;
    private final ShipmentIdDto newShipmentId;
    private final DeliveryStatusDto deliveryStatus;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final RejectReasonDto rejectReason;
    private final Boolean success;
    private final String errorMessage;
    private final TrackingNumber trackingNumber;
    private final TrackingNumber newTrackingNumber;

    public DeliveryRejectResponseDetailsDto(final ShipmentIdDto shipmentId,
                                            final ShipmentIdDto newShipmentId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final RejectReasonDto rejectReason) {
        this(shipmentId, newShipmentId, deliveryStatus, departmentCode, supplierCode, rejectReason, null, null, null, null);
    }

    public DeliveryRejectResponseDetailsDto(final ShipmentIdDto shipmentId,
                                            final ShipmentIdDto newShipmentId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final RejectReasonDto rejectReason,
                                            final Boolean success,
                                            final String errorMessage,
                                            final TrackingNumber trackingNumber,
                                            final TrackingNumber newTrackingNumber) {
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.rejectReason = rejectReason;
        this.success = success;
        this.errorMessage = errorMessage;
        this.trackingNumber = trackingNumber;
        this.newTrackingNumber = newTrackingNumber;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public TrackingNumber getNewTrackingNumber() {
        return newTrackingNumber;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ShipmentIdDto getNewShipmentId() {
        return newShipmentId;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public RejectReasonDto getRejectReason() {
        return rejectReason;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
