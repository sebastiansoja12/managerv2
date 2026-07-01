package com.warehouse.deliveryreject.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public class DeliveryRejectResponseDetailsDto {
    private final ShipmentIdDto shipmentId;
    private final ShipmentIdDto newShipmentId;
    private final DeliveryStatusDto deliveryStatus;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final RejectReasonDto rejectReason;
    private final Boolean success;
    private final String errorMessage;

    public DeliveryRejectResponseDetailsDto(final ShipmentIdDto shipmentId,
                                            final ShipmentIdDto newShipmentId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final RejectReasonDto rejectReason) {
        this(shipmentId, newShipmentId, deliveryStatus, departmentCode, supplierCode, rejectReason, null, null);
    }

    public DeliveryRejectResponseDetailsDto(final ShipmentIdDto shipmentId,
                                            final ShipmentIdDto newShipmentId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final RejectReasonDto rejectReason,
                                            final Boolean success,
                                            final String errorMessage) {
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.rejectReason = rejectReason;
        this.success = success;
        this.errorMessage = errorMessage;
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
