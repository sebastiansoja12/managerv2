package com.warehouse.deliveryreject.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;

public class DeliveryRejectDetailsDto {
    private final ShipmentIdDto shipmentId;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final DeliveryStatusDto deliveryStatus;
    private final RejectReasonDto rejectReason;

    public DeliveryRejectDetailsDto(final ShipmentIdDto shipmentId,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final DeliveryStatusDto deliveryStatus,
                                    final RejectReasonDto rejectReason) {
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public RejectReasonDto getRejectReason() {
        return rejectReason;
    }
}
