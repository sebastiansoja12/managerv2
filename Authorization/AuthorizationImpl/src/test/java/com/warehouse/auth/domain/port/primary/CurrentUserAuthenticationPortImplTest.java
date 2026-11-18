package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.service.CurrentUserAuthenticationService;
import com.warehouse.auth.domain.service.DepartmentService;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrentUserAuthenticationPortImplTest {

    private static final DepartmentId HOME_DEPARTMENT_ID = new DepartmentId(11L);
    private static final DepartmentId OTHER_DEPARTMENT_ID = new DepartmentId(12L);
    private static final DepartmentCode HOME_DEPARTMENT = new DepartmentCode("KT1");
    private static final DepartmentCode OTHER_DEPARTMENT = new DepartmentCode("WAW");

    private final DepartmentServicePort departmentServicePort = mock(DepartmentServicePort.class);

    private final CurrentUserAuthenticationPortImpl port = new CurrentUserAuthenticationPortImpl(
            mock(CurrentUserAuthenticationService.class), new DepartmentService(departmentServicePort));

    @BeforeEach
    void setUp() {
        when(departmentServicePort.getDepartmentCode(HOME_DEPARTMENT_ID)).thenReturn(HOME_DEPARTMENT);
        when(departmentServicePort.getDepartmentCode(OTHER_DEPARTMENT_ID)).thenReturn(OTHER_DEPARTMENT);
        when(departmentServicePort.departmentExists(OTHER_DEPARTMENT)).thenReturn(true);
        when(departmentServicePort.departmentExists(new DepartmentCode("NOPE"))).thenReturn(false);
        when(departmentServicePort.getDepartmentId(OTHER_DEPARTMENT)).thenReturn(OTHER_DEPARTMENT_ID);
    }

    @Test
    void shouldActOnBehalfOfRequestedDepartmentWhenUserIsSuperadmin() {
        final DepartmentCode departmentCode = port.getActingDepartmentCode(user(OrganizationUser.Role.SUPERADMIN),
                Optional.of(OTHER_DEPARTMENT));

        assertThat(departmentCode).isEqualTo(OTHER_DEPARTMENT);
    }

    @Test
    void shouldKeepHomeDepartmentWhenUserIsAdmin() {
        final DepartmentCode departmentCode = port.getActingDepartmentCode(user(OrganizationUser.Role.ADMIN),
                Optional.of(OTHER_DEPARTMENT));

        assertThat(departmentCode).isEqualTo(HOME_DEPARTMENT);
    }

    @Test
    void shouldKeepHomeDepartmentWhenRequestedDepartmentDoesNotExist() {
        final DepartmentCode departmentCode = port.getActingDepartmentCode(user(OrganizationUser.Role.SUPERADMIN),
                Optional.of(new DepartmentCode("NOPE")));

        assertThat(departmentCode).isEqualTo(HOME_DEPARTMENT);
    }

    private OrganizationUser user(final OrganizationUser.Role role) {
        return OrganizationUser.of(new UserId(1L), "user", "password", "First", "Last", "user@example.com", role,
                HOME_DEPARTMENT_ID, null);
    }
}
