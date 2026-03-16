package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public class DeliveryReturnDetailsDto {
    private final ShipmentIdDto shipmentId;
    private final DeliveryStatusDto deliveryStatus;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final ReturnTokenDto returnToken;

    public DeliveryReturnDetailsDto(final ShipmentIdDto shipmentId,
                                    final DeliveryStatusDto deliveryStatus,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final ReturnTokenDto returnToken) {
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.returnToken = returnToken;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
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

    public ReturnTokenDto getReturnToken() {
        return returnToken;
    }

}
