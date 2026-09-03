package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OperatorDirectoryPortImplTest {

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorUserServicePort operatorUserServicePort = mock(OperatorUserServicePort.class);

    private final OperatorDepartmentServicePort operatorDepartmentServicePort = mock(OperatorDepartmentServicePort.class);

    private final OperatorCourierServicePort operatorCourierServicePort = mock(OperatorCourierServicePort.class);

    private final OperatorDirectoryPort operatorDirectoryPort = new OperatorDirectoryPortImpl(operatorServicePort,
            operatorUserServicePort, operatorDepartmentServicePort, operatorCourierServicePort);

    @Test
    void shouldReturnUsersOfSelectedOperator() {
        final OperatorId operatorId = OperatorId.of(10L);
        final OperatorScope operatorScope = new OperatorScope(operatorId, new UserId(1L));
        final OperatorUser user = new OperatorUser(new UserId(2L), "user", "First", "Last", "user@test.pl",
                "USER", new DepartmentCode("KT1"), false);
        when(operatorServicePort.findScope(operatorId)).thenReturn(Optional.of(operatorScope));
        when(operatorUserServicePort.findAll(operatorScope)).thenReturn(List.of(user));

        final List<OperatorUser> users = operatorDirectoryPort.findUsers(operatorId);

        assertThat(users).containsExactly(user);
    }

    @Test
    void shouldThrowExceptionWhenOperatorDoesNotExist() {
        final OperatorId operatorId = OperatorId.of(99L);
        when(operatorServicePort.findScope(operatorId)).thenReturn(Optional.empty());
        final CreateOperatorDepartmentCommand command = new CreateOperatorDepartmentCommand(new DepartmentCode("KT2"),
                "Katowice", "Testowa 1", "40-001", "PL", "1234567890", "+48123456789", "08:00-16:00",
                "kt2@test.pl", "BRANCH");

        assertThrows(OperatorNotFoundException.class, () -> operatorDirectoryPort.createDepartment(operatorId, command));

        verifyNoInteractions(operatorDepartmentServicePort);
    }
}
