package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mail.api.event.SupplierDriverLicenseInvalidatedIntegrationEvent;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.time.Instant;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class OutputRequestMapper {

    public static SupplierCreatedEvent map(final SupplierSnapshot snapshot, final Consumer<UserId> userCreatedId) {
        return new SupplierCreatedEvent(
                snapshot.supplierId(),
                snapshot.supplierCode(),
                snapshot.firstName(),
                snapshot.lastName(),
                snapshot.email(),
                snapshot.departmentId(),
                userCreatedId);
    }

    public static SupplierDriverLicenseInvalidatedIntegrationEvent map(final Supplier supplier,
                                                                       final Instant invalidatedAt) {
        return new SupplierDriverLicenseInvalidatedIntegrationEvent(
                supplier.supplierCode(),
                supplier.getEmail(),
                supplier.getFirstName(),
                invalidatedAt);
    }

    public static Set<OperatorId> map(final Set<OperatorIdDto> operatorIds) {
        return operatorIds
                .stream()
                .map(operatorId -> new OperatorId(operatorId.value()))
                .collect(Collectors.toSet());
    }

    public static DepartmentId mapDepartmentId(final DepartmentDto department) {
        return new DepartmentId(department.departmentId());
    }

    public static DepartmentCode mapDepartmentCode(final DepartmentDto department) {
        return new DepartmentCode(department.departmentCode());
    }
}
