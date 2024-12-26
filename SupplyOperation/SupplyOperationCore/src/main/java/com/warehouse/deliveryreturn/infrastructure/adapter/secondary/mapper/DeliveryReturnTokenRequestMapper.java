package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.ReturnPackageRequest;
import com.warehouse.deliveryreturn.domain.vo.Supplier;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.*;

@Mapper
public interface DeliveryReturnTokenRequestMapper {

    default ReturnTokenRequestDto map(final ReturnTokenRequest returnTokenRequest) {
        final SupplierDto supplier = map(returnTokenRequest.getSupplier());
        final List<ReturnPackageRequestDto> returnPackageRequests = returnTokenRequest
                .getReturnPackageRequests()
                .stream()
                .map(this::map)
                .toList();
        return new ReturnTokenRequestDto(returnPackageRequests, supplier);
    }

    default ReturnPackageRequestDto map(final ReturnPackageRequest returnPackageRequest) {
        final UUID id = returnPackageRequest.getId();
        final ShipmentIdDto shipmentId = map(returnPackageRequest.getShipmentId());
        final DepartmentCodeDto departmentCode = map(returnPackageRequest.getDepartmentCode());
        final SupplierCodeDto supplierCode = map(returnPackageRequest.getSupplierCode());
        final String deliveryStatus = returnPackageRequest.getDeliveryStatus().name();
        return new ReturnPackageRequestDto(id, shipmentId, departmentCode, supplierCode, deliveryStatus, returnPackageRequest.isLocked());
    }

    default SupplierCodeDto map(final SupplierCode supplierCode) {
        return new SupplierCodeDto(supplierCode.getValue());
    }

    default SupplierDto map(final Supplier supplier) {
        final SupplierCodeDto supplierCode = map(supplier.getSupplierCode());
        return new SupplierDto(supplierCode);
    }

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }

    default DepartmentCodeDto map(final DepartmentCode departmentCode) {
        return new DepartmentCodeDto(departmentCode.getValue());
    }

}
