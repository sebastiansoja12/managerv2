package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.AddressResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorCourierResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDepartmentResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorUserResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ValueResponse;

public abstract class OperatorDirectoryResponseMapper {

    public static OperatorUserResponse toResponse(final OperatorUser user) {
        return new OperatorUserResponse(
                new ValueResponse<>(user.userId().value()),
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.role(),
                user.departmentCode() != null ? user.departmentCode().getValue() : null,
                user.deleted());
    }

    public static OperatorDepartmentResponse toResponse(final OperatorDepartment department) {
        return new OperatorDepartmentResponse(
                department.departmentId().value(),
                new ValueResponse<>(department.departmentCode().getValue()),
                new AddressResponse(department.city(), department.street(), department.postalCode(),
                        department.countryCode()),
                department.taxId(),
                department.telephoneNumber(),
                department.openingHours(),
                department.email(),
                department.departmentType(),
                department.status());
    }

    public static OperatorCourierResponse toResponse(final OperatorCourier courier) {
        return new OperatorCourierResponse(
                courier.supplierId() != null ? courier.supplierId().value() : null,
                new ValueResponse<>(courier.supplierCode().value()),
                courier.firstName(),
                courier.lastName(),
                courier.telephoneNumber(),
                courier.departmentCode() != null ? new ValueResponse<>(courier.departmentCode().getValue()) : null,
                courier.status());
    }
}
