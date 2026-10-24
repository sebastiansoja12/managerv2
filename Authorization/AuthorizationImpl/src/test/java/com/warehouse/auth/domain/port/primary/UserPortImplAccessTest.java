package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.exception.UserAccessDeniedException;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserPortImplAccessTest {

    private static final DepartmentId OWN_DEPARTMENT = new DepartmentId(10L);

    private static final DepartmentId OTHER_DEPARTMENT = new DepartmentId(20L);

    private final UserService userService = mock(UserService.class);

    private final AuthenticationService authenticationService = mock(AuthenticationService.class);

    private final UserPort userPort = new UserPortImpl(userService, authenticationService, mock(ApiKeyEncoder.class));

    @Test
    void shouldReturnUsersOfAllDepartmentsForDirectory() {
        final OrganizationUser admin = givenCurrentUser(OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        final OrganizationUser otherDepartmentUser = user(2L, OrganizationUser.Role.USER, OTHER_DEPARTMENT);
        when(userService.findAll()).thenReturn(List.of(admin, otherDepartmentUser));

        assertThat(userPort.findAll()).containsExactly(admin, otherDepartmentUser);
    }

    @Test
    void shouldNotAllowAdminToEditUserFromOtherDepartment() {
        givenCurrentUser(OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.USER, OTHER_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);

        assertThrows(UserAccessDeniedException.class, () -> userPort.update(updateCommand(target.getUserId(), "WA1")));

        verify(userService, never()).update(any());
    }

    @Test
    void shouldNotAllowAdminToMoveUserToOtherDepartment() {
        givenCurrentUser(OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.USER, OWN_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);
        when(userService.getDepartmentId(new DepartmentCode("WA1"))).thenReturn(OTHER_DEPARTMENT);

        assertThrows(UserAccessDeniedException.class, () -> userPort.update(updateCommand(target.getUserId(), "WA1")));
    }

    @Test
    void shouldAllowSuperadminToEditUserFromAnyDepartment() {
        givenCurrentUser(OrganizationUser.Role.SUPERADMIN, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.ADMIN, OTHER_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);
        when(userService.getDepartmentId(new DepartmentCode("WA1"))).thenReturn(OTHER_DEPARTMENT);
        final UpdateUserCommand command = updateCommand(target.getUserId(), "WA1");

        userPort.update(command);

        verify(userService).update(command);
    }

    @Test
    void shouldNotAllowAdminToAssignSuperadminRole() {
        givenCurrentUser(OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.USER, OWN_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);

        assertThrows(UserAccessDeniedException.class,
                () -> userPort.changeRole(target.getUserId(), OrganizationUser.Role.SUPERADMIN));

        verify(userService, never()).changeRole(any(), any());
    }

    @Test
    void shouldAllowAdminToPromoteUserToManagerInOwnDepartment() {
        givenCurrentUser(OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.USER, OWN_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);

        userPort.changeRole(target.getUserId(), OrganizationUser.Role.MANAGER);

        verify(userService).changeRole(target.getUserId(), OrganizationUser.Role.MANAGER);
    }

    @Test
    void shouldNotAllowManagerToChangePermissionsOfAdmin() {
        givenCurrentUser(OrganizationUser.Role.MANAGER, OWN_DEPARTMENT);
        final OrganizationUser target = user(2L, OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);
        when(userService.findUserById(target.getUserId())).thenReturn(target);

        assertThrows(UserAccessDeniedException.class,
                () -> userPort.removePermission(target.getUserId(), "ROLE_MANAGER_READ"));
    }

    private OrganizationUser givenCurrentUser(final OrganizationUser.Role role, final DepartmentId departmentId) {
        final OrganizationUser currentUser = user(1L, role, departmentId);
        when(authenticationService.currentUserId()).thenReturn(currentUser.getUserId());
        when(userService.findUserById(currentUser.getUserId())).thenReturn(currentUser);
        return currentUser;
    }

    private OrganizationUser user(final Long userId, final OrganizationUser.Role role, final DepartmentId departmentId) {
        final OrganizationUser user = OrganizationUser.of(new UserId(userId), "user-" + userId, "password", "First",
                "Last", "user" + userId + "@test.pl", role, departmentId, "api-key");
        user.assignOperator(OperatorId.of(1L));
        return user;
    }

    private UpdateUserCommand updateCommand(final UserId userId, final String departmentCode) {
        return new UpdateUserCommand(userId, "First", "Last", "user", "user@test.pl",
                new DepartmentCode(departmentCode), "pl");
    }
}
