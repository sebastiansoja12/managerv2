package com.warehouse.department.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.event.OperatorInitialDepartmentCreateEvent;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateCommand;
import com.warehouse.department.domain.port.primary.DepartmentPort;

class OperatorDepartmentEventListenerTest {

    @Test
    void shouldPassReservedAdminUserIdToInitialDepartment() {
        final DepartmentPort departmentPort = mock(DepartmentPort.class);
        final OperatorDepartmentEventListener listener = new OperatorDepartmentEventListener(departmentPort);
        final UserId adminUserId = new UserId(77L);
        final OperatorInitialDepartmentCreateEvent event = new OperatorInitialDepartmentCreateEvent(
                OperatorId.of(500L),
                adminUserId,
                new DepartmentCode("TST"),
                "Test Logistics",
                "1234567890",
                "+48123456789",
                "contact@test.pl",
                "Wroclaw",
                "Testowa 1",
                "50-001",
                "PL",
                "08:00-16:00",
                "HEADQUARTERS"
        );

        listener.handle(event);

        final ArgumentCaptor<DepartmentCreateCommand> commandCaptor =
                ArgumentCaptor.forClass(DepartmentCreateCommand.class);
        verify(departmentPort).createDepartments(commandCaptor.capture());
        final DepartmentCreate department = commandCaptor.getValue().getDepartments().getFirst();
        assertEquals(adminUserId, department.getAdminUserId());
        assertEquals(event.operatorId(), department.getOperatorId());
    }
}
