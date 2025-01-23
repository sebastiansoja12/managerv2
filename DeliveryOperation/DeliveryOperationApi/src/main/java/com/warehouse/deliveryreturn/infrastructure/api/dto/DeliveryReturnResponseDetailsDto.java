package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public class DeliveryReturnResponseDetailsDto {
    private final ProcessIdDto processId;
    private final ShipmentIdDto shipmentId;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final DeliveryStatusDto deliveryStatus;
    private final ReturnTokenDto returnToken;
    private final UpdateStatusDto updateStatus;


    public DeliveryReturnResponseDetailsDto(final ProcessIdDto processId,
                                            final ShipmentIdDto shipmentId,
                                            final DepartmentCodeDto departmentCode,
                                            final SupplierCodeDto supplierCode,
                                            final DeliveryStatusDto deliveryStatus,
                                            final ReturnTokenDto returnToken,
                                            final UpdateStatusDto updateStatus) {
        this.processId = processId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
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
}
