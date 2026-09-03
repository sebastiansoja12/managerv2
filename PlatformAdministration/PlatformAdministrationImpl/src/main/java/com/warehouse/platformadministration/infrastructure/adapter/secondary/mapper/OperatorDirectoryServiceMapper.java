package com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.api.dto.CreateDepartmentApiRequest;
import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDetailsDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentTypeDto;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorUser;

public abstract class OperatorDirectoryServiceMapper {

    public static OperatorUser toOperatorUser(final UserDto user) {
        return new OperatorUser(
                new UserId(user.userId().value()),
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.role(),
                user.departmentCode() != null ? new DepartmentCode(user.departmentCode()) : null,
                Boolean.TRUE.equals(user.deleted()));
    }

    public static CreateOperatorUserApiRequest toCreateOperatorUserApiRequest(final CreateOperatorUserCommand command) {
        return new CreateOperatorUserApiRequest(
                command.firstName(),
                command.lastName(),
                command.username(),
                command.password(),
                command.email(),
                command.role(),
                command.departmentCode().getValue(),
                command.language());
    }

    public static OperatorDepartment toOperatorDepartment(final DepartmentDetailsDto department) {
        return new OperatorDepartment(
                new DepartmentId(department.departmentId().value()),
                new DepartmentCode(department.departmentCode().value()),
                department.city(),
                department.street(),
                department.postalCode(),
                department.countryCode(),
                department.taxId(),
                department.telephoneNumber(),
                department.openingHours(),
                department.email(),
                department.departmentType().name(),
                department.status().name());
    }

    public static CreateDepartmentApiRequest toCreateDepartmentApiRequest(final CreateOperatorDepartmentCommand command) {
        return new CreateDepartmentApiRequest(
                new DepartmentCodeDto(command.departmentCode().getValue()),
                command.city(),
                command.street(),
                command.postalCode(),
                command.countryCode(),
                command.taxId(),
                command.telephoneNumber(),
                command.openingHours(),
                command.email(),
                DepartmentTypeDto.valueOf(command.departmentType()));
    }

    public static OperatorCourier toOperatorCourier(final SupplierDetailsDto supplier) {
        return new OperatorCourier(
                supplier.supplierId() != null ? new SupplierId(supplier.supplierId()) : null,
                new SupplierCode(supplier.supplierCode().value()),
                supplier.firstName(),
                supplier.lastName(),
                supplier.telephoneNumber(),
                supplier.departmentCode() != null ? new DepartmentCode(supplier.departmentCode()) : null,
                supplier.status());
    }

    public static CreateSupplierApiRequest toCreateSupplierApiRequest(final CreateOperatorCourierCommand command) {
        return new CreateSupplierApiRequest(
                command.supplierCode() != null ? new SupplierCodeDto(command.supplierCode().value()) : null,
                command.firstName(),
                command.lastName(),
                command.telephoneNumber(),
                command.departmentCode().getValue());
    }
}
