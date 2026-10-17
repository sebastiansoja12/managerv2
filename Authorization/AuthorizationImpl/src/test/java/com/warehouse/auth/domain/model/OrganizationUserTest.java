package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.service.RolePermissionService;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrganizationUserTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(1L);
    private static final DepartmentId OWN_DEPARTMENT = new DepartmentId(10L);
    private static final DepartmentId OTHER_DEPARTMENT = new DepartmentId(20L);

    @Test
    void shouldCreateClassMatchingRole() {
        assertThat(user(1L, OrganizationUser.Role.USER, OWN_DEPARTMENT)).isInstanceOf(User.class);
        assertThat(user(2L, OrganizationUser.Role.SUPPLIER, OWN_DEPARTMENT)).isInstanceOf(Supplier.class);
        assertThat(user(3L, OrganizationUser.Role.MANAGER, OWN_DEPARTMENT)).isInstanceOf(Manager.class);
        assertThat(user(4L, OrganizationUser.Role.ADMIN, OWN_DEPARTMENT)).isInstanceOf(Admin.class);
        assertThat(user(5L, OrganizationUser.Role.SUPERADMIN, OWN_DEPARTMENT)).isInstanceOf(Superadmin.class);
    }

    @Test
    void shouldOrderAccountLevelsByHierarchy() {
        assertThat(AccountLevel.PLATFORM_OWNER.isHigherThan(AccountLevel.SUPERADMIN)).isTrue();
        assertThat(AccountLevel.SUPERADMIN.isHigherThan(AccountLevel.ADMIN)).isTrue();
        assertThat(AccountLevel.ADMIN.isHigherThan(AccountLevel.MANAGER)).isTrue();
        assertThat(AccountLevel.MANAGER.isHigherThan(AccountLevel.USER)).isTrue();
        assertThat(user(1L, OrganizationUser.Role.SUPPLIER, OWN_DEPARTMENT).getLevel()).isEqualTo(AccountLevel.USER);
    }

    @Test
    void shouldLimitAdminToOwnDepartment() {
        final OrganizationUser admin = user(1L, OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);

        assertThat(admin.canAccessDepartment(OWN_DEPARTMENT)).isTrue();
        assertThat(admin.canAccessDepartment(OTHER_DEPARTMENT)).isFalse();
    }

    @Test
    void shouldAllowSuperadminToAccessEveryDepartment() {
        final OrganizationUser superadmin = user(1L, OrganizationUser.Role.SUPERADMIN, OWN_DEPARTMENT);

        assertThat(superadmin.canAccessDepartment(OTHER_DEPARTMENT)).isTrue();
    }

    @Test
    void shouldAllowManagingOnlyLowerLevelUsersInOwnDepartment() {
        final OrganizationUser admin = user(1L, OrganizationUser.Role.ADMIN, OWN_DEPARTMENT);

        assertThat(admin.canManage(user(2L, OrganizationUser.Role.MANAGER, OWN_DEPARTMENT))).isTrue();
        assertThat(admin.canManage(user(3L, OrganizationUser.Role.MANAGER, OTHER_DEPARTMENT))).isFalse();
        assertThat(admin.canManage(user(4L, OrganizationUser.Role.ADMIN, OWN_DEPARTMENT))).isFalse();
        assertThat(admin.canAssignRole(OrganizationUser.Role.MANAGER)).isTrue();
        assertThat(admin.canAssignRole(OrganizationUser.Role.SUPERADMIN)).isFalse();
    }

    @Test
    void shouldAllowSuperadminToManageEveryUserOfOperator() {
        final OrganizationUser superadmin = user(1L, OrganizationUser.Role.SUPERADMIN, OWN_DEPARTMENT);
        final OrganizationUser otherOperatorUser = user(3L, OrganizationUser.Role.USER, OWN_DEPARTMENT);
        otherOperatorUser.assignOperator(OperatorId.of(2L));

        assertThat(superadmin.canManage(user(2L, OrganizationUser.Role.ADMIN, OTHER_DEPARTMENT))).isTrue();
        assertThat(superadmin.canAssignRole(OrganizationUser.Role.SUPERADMIN)).isTrue();
        assertThat(superadmin.canManage(otherOperatorUser)).isFalse();
    }

    @Test
    void shouldNotAllowUserToManageAnyone() {
        final OrganizationUser user = user(1L, OrganizationUser.Role.USER, OWN_DEPARTMENT);

        assertThat(user.canManage(user(2L, OrganizationUser.Role.SUPPLIER, OWN_DEPARTMENT))).isFalse();
        assertThat(user.canAssignRole(OrganizationUser.Role.USER)).isFalse();
    }

    @Test
    void shouldChangeClassWhenRoleChanges() {
        givenDomainRegistry();
        final OrganizationUser manager = user(1L, OrganizationUser.Role.MANAGER, OWN_DEPARTMENT);
        manager.setDeleted(false);

        final OrganizationUser admin = manager.changeRole(OrganizationUser.Role.ADMIN);

        assertThat(admin).isInstanceOf(Admin.class);
        assertThat(admin.getUserId()).isEqualTo(manager.getUserId());
        assertThat(admin.operatorId()).isEqualTo(OPERATOR_ID);
        assertThat(admin.getDepartmentId()).isEqualTo(OWN_DEPARTMENT);
    }

    @Test
    void shouldGivePlatformOwnerAccessToOperatorPanelOnly() {
        final PlatformOwner platformOwner = new PlatformOwner(null, "owner", "password", "Platform", "Owner",
                "owner@test.pl", false, null, null);

        assertThat(platformOwner.canAccessOperatorPanel()).isTrue();
        assertThat(platformOwner.getLevel()).isEqualTo(AccountLevel.PLATFORM_OWNER);
        assertThat(user(1L, OrganizationUser.Role.SUPERADMIN, OWN_DEPARTMENT).canAccessOperatorPanel()).isFalse();
    }

    private OrganizationUser user(final Long userId, final OrganizationUser.Role role, final DepartmentId departmentId) {
        final OrganizationUser user = OrganizationUser.of(new UserId(userId), "user-" + userId, "password", "First",
                "Last", "user" + userId + "@test.pl", role, departmentId, "api-key");
        user.assignOperator(OPERATOR_ID);
        return user;
    }

    private void givenDomainRegistry() {
        final RolePermissionService rolePermissionService = mock(RolePermissionService.class);
        when(rolePermissionService.findAllAdminPermissions()).thenReturn(Set.of());
        final StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.getBeanFactory().registerSingleton("rolePermissionService", rolePermissionService);
        final DomainRegistry domainRegistry = new DomainRegistry();
        domainRegistry.setApplicationContext(applicationContext);
        domainRegistry.setApplicationEventPublisher(event -> {
        });
    }
}
