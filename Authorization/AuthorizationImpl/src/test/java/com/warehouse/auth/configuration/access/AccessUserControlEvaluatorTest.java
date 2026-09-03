package com.warehouse.auth.configuration.access;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccessUserControlEvaluatorTest {

    private static final UserId USER_ID = new UserId(7L);

    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    private final UserService userService = mock(UserService.class);

    private final DepartmentApiService departmentApiService = mock(DepartmentApiService.class);

    private final AccessUserControlEvaluator evaluator = new AccessUserControlEvaluator(userService, departmentApiService);

    @BeforeEach
    void setUp() {
        when(departmentApiService.getDepartmentById(DEPARTMENT_ID))
                .thenReturn(new DepartmentDto(10L, "KT1", "Katowice", "Testowa 1", "40-001", "PL", null));
    }

    @Test
    void shouldAllowPlatformOwnerOnPlatformOwnerEndpoint() {
        assertThatCode(() -> evaluator.validate(platformOwnerAuthentication(), annotation("platformOwnerOnly")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRejectPlatformOwnerOnOrganizationEndpoint() {
        final ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> evaluator.validate(platformOwnerAuthentication(), annotation("organizationUser")));

        assertThat(exception.getStatus()).isEqualTo(403);
    }

    @Test
    void shouldRejectOrganizationUserOnPlatformOwnerEndpoint() {
        final ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> evaluator.validate(userAuthentication(), annotation("platformOwnerOnly")));

        assertThat(exception.getStatus()).isEqualTo(403);
    }

    @Test
    void shouldAllowBothAccountTypesOnAnyAccountEndpoint() {
        when(userService.findUserById(USER_ID)).thenReturn(user(OrganizationUser.Role.USER));

        assertThatCode(() -> evaluator.validate(userAuthentication(), annotation("anyAccount")))
                .doesNotThrowAnyException();
        assertThatCode(() -> evaluator.validate(platformOwnerAuthentication(), annotation("anyAccount")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRejectUserBelowMinimumLevel() {
        when(userService.findUserById(USER_ID)).thenReturn(user(OrganizationUser.Role.MANAGER));

        final ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> evaluator.validate(userAuthentication(), annotation("adminOnly")));

        assertThat(exception.getStatus()).isEqualTo(403);
    }

    @Test
    void shouldAllowSuperadminWhenAdminLevelIsRequired() {
        when(userService.findUserById(USER_ID)).thenReturn(user(OrganizationUser.Role.SUPERADMIN));

        assertThatCode(() -> evaluator.validate(userAuthentication(), annotation("adminOnly")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRejectDeletedUser() {
        final OrganizationUser user = user(OrganizationUser.Role.ADMIN);
        user.setDeleted(true);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> evaluator.validate(userAuthentication(), annotation("organizationUser")));

        assertThat(exception.getStatus()).isEqualTo(403);
    }

    private Authentication platformOwnerAuthentication() {
        return new UsernamePasswordAuthenticationToken(new PlatformOwnerId(1L), null,
                List.of(new SimpleGrantedAuthority("ROLE_PLATFORM_OWNER")));
    }

    private Authentication userAuthentication() {
        return new UsernameTenantPasswordAuthenticationToken(USER_ID, OperatorId.of(1L), DEPARTMENT_ID, null, List.of());
    }

    private OrganizationUser user(final OrganizationUser.Role role) {
        final OrganizationUser user = OrganizationUser.of(USER_ID, "user", "password", "First", "Last",
                "user@test.pl", role, DEPARTMENT_ID, "api-key");
        user.assignOperator(OperatorId.of(1L));
        return user;
    }

    private AccessUserControl annotation(final String methodName) {
        try {
            return AnnotatedEndpoints.class.getDeclaredMethod(methodName).getAnnotation(AccessUserControl.class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static final class AnnotatedEndpoints {

        @AccessUserControl
        void organizationUser() {
        }

        @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
        void platformOwnerOnly() {
        }

        @AccessUserControl(audience = AccessAudience.ANY_ACCOUNT)
        void anyAccount() {
        }

        @AccessUserControl(minimumLevel = AccountLevel.ADMIN)
        void adminOnly() {
        }
    }
}
