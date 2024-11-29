package com.warehouse.deliveryreturn.infrastructure.api.dto;


import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

import java.util.List;

public class DeliveryReturnResponseDto {
    private final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponses;
    private final SupplierCodeDto supplierCode;
    private final DepartmentCodeDto departmentCode;
    private final DeviceInformationDto deviceInformation;

    public DeliveryReturnResponseDto(final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponses,
                                     final SupplierCodeDto supplierCode,
                                     final DepartmentCodeDto departmentCode,
                                     final DeviceInformationDto deviceInformation) {
        this.deliveryReturnResponses = deliveryReturnResponses;
        this.supplierCode = supplierCode;
        this.departmentCode = departmentCode;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryReturnResponseDetailsDto> getDeliveryReturnResponses() {
        return deliveryReturnResponses;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public DeviceInformationDto getDeviceInformation() {
        return deviceInformation;
    }
}
