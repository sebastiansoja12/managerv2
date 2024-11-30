package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public class DeliveryReturnDetailsDto {
    private ShipmentIdDto shipmentId;
    private DeliveryStatusDto deliveryStatus;
    private DepartmentCodeDto departmentCode;
    private SupplierCodeDto supplierCode;
    private ReturnTokenDto returnTokenDto;

    public DeliveryReturnDetailsDto() {
    }

    public DeliveryReturnDetailsDto(final ShipmentIdDto shipmentId,
                                    final DeliveryStatusDto deliveryStatus,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final ReturnTokenDto returnTokenDto) {
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.returnTokenDto = returnTokenDto;
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

    public ReturnTokenDto getReturnTokenDto() {
        return returnTokenDto;
    }
}
