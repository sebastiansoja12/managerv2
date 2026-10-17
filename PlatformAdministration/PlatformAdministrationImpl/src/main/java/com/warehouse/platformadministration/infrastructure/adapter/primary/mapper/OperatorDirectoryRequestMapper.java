package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorCourierRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorDepartmentRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreateOperatorUserRequest;

public abstract class OperatorDirectoryRequestMapper {

    public static CreateOperatorUserCommand toCommand(final CreateOperatorUserRequest request) {
        return new CreateOperatorUserCommand(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.password(),
                request.email(),
                request.role(),
                new DepartmentCode(request.departmentCode()),
                request.language());
    }

    public static CreateOperatorDepartmentCommand toCommand(final CreateOperatorDepartmentRequest request) {
        return new CreateOperatorDepartmentCommand(
                new DepartmentCode(request.departmentCode()),
                request.city(),
                request.street(),
                request.postalCode(),
                request.countryCode(),
                request.taxId(),
                request.telephoneNumber(),
                request.openingHours(),
                request.email(),
                request.departmentType());
    }

    public static CreateOperatorCourierCommand toCommand(final CreateOperatorCourierRequest request) {
        return new CreateOperatorCourierCommand(
                request.supplierCode() == null || request.supplierCode().isBlank()
                        ? null
                        : new SupplierCode(request.supplierCode()),
                request.firstName(),
                request.lastName(),
                request.telephoneNumber(),
                new DepartmentCode(request.departmentCode()));
    }
}
