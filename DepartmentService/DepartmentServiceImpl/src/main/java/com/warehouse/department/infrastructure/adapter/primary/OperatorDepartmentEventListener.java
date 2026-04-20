package com.warehouse.department.infrastructure.adapter.primary;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.api.event.OperatorInitialDepartmentCreateEvent;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateCommand;
import com.warehouse.department.domain.port.primary.DepartmentPort;

@Component
public class OperatorDepartmentEventListener {

    private final DepartmentPort departmentPort;

    public OperatorDepartmentEventListener(final DepartmentPort departmentPort) {
        this.departmentPort = departmentPort;
    }

    @EventListener
    public void handle(final OperatorInitialDepartmentCreateEvent event) {
        final DepartmentCreate department = new DepartmentCreate(
                event.departmentCode(),
                event.city(),
                event.street(),
                event.postalCode(),
                event.taxId(),
                event.contactPhone(),
                event.openingHours(),
                event.contactEmail(),
                CountryCode.valueOf(event.countryCode()),
                DepartmentType.valueOf(event.departmentType()),
                event.operatorId()
        );
        departmentPort.createDepartments(new DepartmentCreateCommand(List.of(department)));
    }
}
