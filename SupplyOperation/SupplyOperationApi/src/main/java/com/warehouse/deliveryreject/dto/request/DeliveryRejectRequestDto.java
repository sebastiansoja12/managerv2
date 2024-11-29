package com.warehouse.deliveryreject.dto.request;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliveryreject.dto.*;

public class DeliveryRejectRequestDto {
    private final DeviceIdDto deviceId;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final DeliveryStatusDto deliveryStatus;
    private final ShipmentIdDto shipmentId;
    private final RejectReasonDto rejectReason;

    public DeliveryRejectRequestDto(final DeviceIdDto deviceId,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final DeliveryStatusDto deliveryStatus,
                                    final ShipmentIdDto shipmentId,
                                    final RejectReasonDto rejectReason) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.shipmentId = shipmentId;
        this.rejectReason = rejectReason;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
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

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public RejectReasonDto getRejectReason() {
        return rejectReason;
    }
}
