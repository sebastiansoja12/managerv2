package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.event.OperatorInitialDepartmentCreateEvent;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorProvisioningDetails;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public final class OperatorDepartmentNotifyMapper {

    private OperatorDepartmentNotifyMapper() {
    }

    public static OperatorInitialDepartmentCreateEvent toEvent(final OperatorSnapshot snapshot) {
        final OperatorProvisioningDetails.FirstDepartment firstDepartment = snapshot.provisioningDetails().firstDepartment();
        return new OperatorInitialDepartmentCreateEvent(
                snapshot.operatorId(),
                new DepartmentCode(firstDepartment.departmentCode()),
                snapshot.companyName(),
                snapshot.taxId().value(),
                snapshot.contactPhone(),
                snapshot.contactEmail(),
                firstDepartment.city(),
                firstDepartment.street(),
                firstDepartment.postalCode(),
                firstDepartment.countryCode(),
                firstDepartment.openingHours(),
                firstDepartment.departmentType()
        );
    }
}
