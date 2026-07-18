package com.warehouse.auth.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.RolePermissionService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.event.OperatorCreatedEvent;
import com.warehouse.auth.infrastructure.dto.DepartmentCodeDto;
import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.auth.infrastructure.dto.RegisteringUserDto;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

class AuthEventListenerTest {

    private final AuthenticationPort authenticationPort = mock(AuthenticationPort.class);
    private final UserPort userPort = mock(UserPort.class);
    private final UserService userService = mock(UserService.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private AuthEventListener listener;

    @BeforeEach
    void setUp() {
        final RolePermissionService rolePermissionService = mock(RolePermissionService.class);
        final ApplicationContext applicationContext = mock(ApplicationContext.class);
        final DomainRegistry domainRegistry = new DomainRegistry();

        when(applicationContext.getBean(RolePermissionService.class)).thenReturn(rolePermissionService);
        when(rolePermissionService.findAllAdminPermissions()).thenReturn(Set.of(
                new RolePermission(new RolePermissionId(1L), UserPermission.ROLE_ADMIN_UPDATE)
        ));
        domainRegistry.setApplicationContext(applicationContext);

        final ApiKeyEncoder apiKeyEncoder = new ApiKeyEncoder() {
            @Override
            public com.warehouse.auth.domain.vo.ApiKey encode(final User user) {
                return new com.warehouse.auth.domain.vo.ApiKey(user.getUserId(), "encoded-api-key");
            }

            @Override
            public com.warehouse.auth.domain.vo.ApiKey encode(final UserId userId, final String username) {
                return new com.warehouse.auth.domain.vo.ApiKey(userId, "encoded-api-key");
            }

            @Override
            public com.warehouse.auth.domain.vo.ApiKey decode(final com.warehouse.auth.domain.vo.ApiKey apiKey) {
                return new com.warehouse.auth.domain.vo.ApiKey(apiKey.userId(), "encoded-api-key");
            }
        };
        listener = new AuthEventListener(authenticationPort, userPort, userService, jwtService, passwordEncoder,
                apiKeyEncoder);
    }

    @Test
    void shouldCreateInitialAdminUserAfterOperatorCreatedEvent() {
        final AtomicReference<UserId> publishedUserId = new AtomicReference<>();
        final RegisteringUserDto registeringUser = new RegisteringUserDto(
                "Sebastian",
                "Soja",
                "s-soja",
                "raw-password",
                "PL",
                "sebastian@test.pl",
                new DepartmentCodeDto("TST"),
                new OperatorIdDto(500L)
        );
        final OperatorCreatedEvent event = new OperatorCreatedEvent(registeringUser, publishedUserId::set);

        when(userService.nextUserId()).thenReturn(new UserId(77L));
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");
        when(userService.create(any(User.class))).thenReturn(new RegisterResponse(UserResponse.builder().build()));

        listener.handle(event);

        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(userCaptor.capture());
        final User createdUser = userCaptor.getValue();
        assertThat(createdUser.getUserId()).isEqualTo(new UserId(77L));
        assertThat(createdUser.operatorId()).isEqualTo(OperatorId.of(500L));
        assertThat(createdUser.getRole()).isEqualTo(User.Role.ADMIN);
        assertThat(createdUser.isInitial()).isTrue();
        assertThat(createdUser.getPassword()).isEqualTo("encoded-password");
        assertThat(createdUser.getApiKey()).isEqualTo("encoded-api-key");
        assertThat(publishedUserId.get()).isEqualTo(new UserId(77L));
    }
}
