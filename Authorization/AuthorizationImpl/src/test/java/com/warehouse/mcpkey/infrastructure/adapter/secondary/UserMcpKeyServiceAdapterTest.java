package com.warehouse.mcpkey.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.mcpkey.McpAccessFixture;
import com.warehouse.mcpkey.McpKeyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMcpKeyServiceAdapterTest {

    private final UserService userService = mock(UserService.class);
    private final CurrentUserApiService currentUser = mock(CurrentUserApiService.class);
    private final UserMcpKeyServiceAdapter adapter = new UserMcpKeyServiceAdapter(userService, currentUser);

    @BeforeEach
    void setUp() {
        when(currentUser.getCurrentUserId()).thenReturn(McpAccessFixture.ADMIN_ID);
        when(userService.findUserById(McpAccessFixture.ADMIN_ID)).thenReturn(McpAccessFixture.user(
                McpAccessFixture.ADMIN_ID, OrganizationUser.Role.ADMIN, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));
    }

    @Test
    void shouldAllowAdministratorToManageUserInOwnScope() {
        when(userService.findUserById(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessFixture.user(
                McpKeyFixture.OWNER_ID, OrganizationUser.Role.USER, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));

        assertThatCode(() -> adapter.requireManageableUser(McpKeyFixture.OWNER_ID)).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectUserFromAnotherOperatorEvenWithSameDepartment() {
        when(userService.findUserById(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessFixture.user(
                McpKeyFixture.OWNER_ID, OrganizationUser.Role.USER, OperatorId.of(99L), new DepartmentId(1L)));

        assertThatThrownBy(() -> adapter.requireManageableUser(McpKeyFixture.OWNER_ID))
                .isInstanceOf(ProblemDetailsException.class);
    }

    @Test
    void shouldRejectUserFromAnotherDepartment() {
        when(userService.findUserById(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessFixture.user(
                McpKeyFixture.OWNER_ID, OrganizationUser.Role.USER, McpKeyFixture.OPERATOR_ID, new DepartmentId(2L)));

        assertThatThrownBy(() -> adapter.requireManageableUser(McpKeyFixture.OWNER_ID))
                .isInstanceOf(ProblemDetailsException.class);
    }

    @Test
    void shouldRejectHigherRoleAndMissingUser() {
        assertThatThrownBy(() -> adapter.requireManageableUser(McpKeyFixture.OWNER_ID))
                .isInstanceOf(ProblemDetailsException.class);
        when(userService.findUserById(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessFixture.user(
                McpKeyFixture.OWNER_ID, OrganizationUser.Role.SUPERADMIN, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));

        assertThatThrownBy(() -> adapter.requireManageableUser(McpKeyFixture.OWNER_ID))
                .isInstanceOf(ProblemDetailsException.class);
    }

    @Test
    void shouldRejectManagerEvenWhenTargetIsSelf() {
        when(userService.findUserById(McpAccessFixture.ADMIN_ID)).thenReturn(McpAccessFixture.user(
                McpAccessFixture.ADMIN_ID, OrganizationUser.Role.MANAGER, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));

        assertThatThrownBy(() -> adapter.requireManageableUser(McpAccessFixture.ADMIN_ID))
                .isInstanceOf(ProblemDetailsException.class);
    }
}
