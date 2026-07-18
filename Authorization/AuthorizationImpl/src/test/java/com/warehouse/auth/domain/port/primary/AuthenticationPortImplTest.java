package com.warehouse.auth.domain.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.CreateOperatorUserCommand;
import com.warehouse.auth.domain.model.CreateUserCommand;
import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.service.ApiEncoderImpl;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.service.DepartmentService;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.auth.domain.service.RefreshTokenGeneratorImpl;
import com.warehouse.auth.domain.service.RolePermissionService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.service.UserServiceImpl;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

class AuthenticationPortImplTest {

    private InMemoryUserRepository userRepository;
    private InMemoryRefreshTokenRepository refreshTokenRepository;
    private AuthenticationPortImpl port;
    private JwtService jwtService;
    private ApiKeyEncoder apiKeyEncoder;
    private PasswordEncoder passwordEncoder;
    private FakeDepartmentServicePort departmentServicePort;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        refreshTokenRepository = new InMemoryRefreshTokenRepository();
        passwordEncoder = new BCryptPasswordEncoder();
        departmentServicePort = new FakeDepartmentServicePort();

        final JwtProvider jwtProvider = new JwtProvider();
        jwtProvider.setSecretKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        jwtProvider.setExpiration(86400000L);
        jwtProvider.setIssuer("manager-v2");
        jwtProvider.setAudience("manager-v2-gui");

        final RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider();
        refreshTokenProvider.setKey("test-refresh-token-key");
        final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        apiKeyProvider.setKey("test-api-key-secret");

        final UserService userService = new UserServiceImpl(userRepository);
        final RefreshTokenGenerator refreshTokenGenerator = new RefreshTokenGeneratorImpl(refreshTokenProvider);
        final AuthenticationService authenticationService = new AuthenticationServiceImpl(
                refreshTokenRepository,
                refreshTokenGenerator,
                userRepository
        );
        jwtService = new JwtServiceImpl(jwtProvider);
        final DepartmentService departmentService = new DepartmentService(departmentServicePort);
        final MailServicePort mailServicePort = emailNotification -> {
        };
        apiKeyEncoder = new ApiEncoderImpl(apiKeyProvider);
        port = new AuthenticationPortImpl(authenticationService, userService, jwtService, passwordEncoder,
                departmentService, mailServicePort, apiKeyEncoder);

        final StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.getBeanFactory()
                .registerSingleton("testRolePermissionService", new TestRolePermissionService());

        final DomainRegistry domainRegistry = new DomainRegistry();
        domainRegistry.setApplicationContext(applicationContext);
        domainRegistry.setApplicationEventPublisher(new RecordingEventPublisher());
    }

    @Test
    void shouldLoginWithRealServices() {
        givenUser("s-soja", "raw-password", User.Role.ADMIN, OperatorId.of(100L));

        final AuthenticationResponse response = port.login(new LoginRequest("s-soja", "raw-password"));

        assertThat(response.accessToken()).isNotEmpty();
        assertThat(response.refreshToken()).isNotEmpty();
        assertThat(jwtService.extractUsername(response.accessToken())).isEqualTo("s-soja");
        assertThat(refreshTokenRepository.find(response.refreshToken())).isPresent();
    }

    @Test
    void shouldRejectLoginWithInvalidPassword() {
        givenUser("s-soja", "raw-password", User.Role.USER, OperatorId.of(100L));

        assertThrows(AuthenticationErrorException.class,
                () -> port.login(new LoginRequest("s-soja", "bad-password")));
    }

    @Test
    void shouldRefreshAndRotateRefreshTokenWithRealServices() {
        givenUser("s-soja", "raw-password", User.Role.MANAGER, OperatorId.of(100L));
        final AuthenticationResponse loginResponse = port.login(new LoginRequest("s-soja", "raw-password"));

        final AuthenticationResponse refreshResponse = port.refresh(loginResponse.refreshToken());

        assertThat(refreshResponse.accessToken()).isNotEmpty();
        assertThat(jwtService.extractUsername(refreshResponse.accessToken())).isEqualTo("s-soja");
        assertThat(refreshResponse.refreshToken()).isNotEqualTo(loginResponse.refreshToken());
        assertThat(refreshTokenRepository.find(loginResponse.refreshToken())).isEmpty();
        assertThat(refreshTokenRepository.find(refreshResponse.refreshToken())).isPresent();
    }

    @Test
    void shouldCreateOperatorUserInOperatorContextFromCommand() {
        final CreateOperatorUserCommand command = new CreateOperatorUserCommand(
                OperatorId.of(123L),
                "Sebastian",
                "Soja",
                "operator-user",
                "raw-password",
                "sebastian@test.pl",
                "SUPPLIER",
                new DepartmentCode("TST"),
                "PL"
        );

        final UserId userId = port.createOperatorUser(command);

        final User createdUser = userRepository.findByUsername("operator-user");
        assertThat(userId).isEqualTo(createdUser.getUserId());
        assertThat(createdUser.operatorId()).isEqualTo(OperatorId.of(123L));
        assertThat(createdUser.getRole()).isEqualTo(User.Role.SUPPLIER);
        assertThat(passwordEncoder.matches("raw-password", createdUser.getPassword())).isTrue();
        assertThat(createdUser.getDepartmentCode().getValue()).isEqualTo("TST");
        assertThat(createdUser.getApiKey()).isNotNull();
        assertThat(createdUser.getApiKey()).isNotEqualTo("dummy");
        assertThat(createdUser.getApiKey()).doesNotContain("operator-user");
        assertThat(createdUser.getApiKey()).isEqualTo(
                apiKeyEncoder.decode(new ApiKey(userId, "operator-user")).key()
        );
    }

    @Test
    void shouldRejectCreateUserWhenDepartmentDoesNotExist() {
        departmentServicePort.setDepartmentExists(false);
        final CreateUserCommand command = new CreateUserCommand(
                "Sebastian",
                "Soja",
                "operator-user",
                "raw-password",
                "sebastian@test.pl",
                "USER",
                new DepartmentCode("MISSING"),
                "PL"
        );

        assertThrows(AuthenticationErrorException.class, () -> port.createUser(command));
    }

    private void givenUser(final String username,
                           final String rawPassword,
                           final User.Role role,
                           final OperatorId operatorId) {
        final User user = User.createWithRole(
                new UserId((long) userRepository.users.size() + 1),
                username,
                passwordEncoder.encode(rawPassword),
                "Sebastian",
                "Soja",
                username + "@test.pl",
                role,
                new DepartmentCode("TST"),
                "api-key-" + username,
                "PL"
        );
        user.assignOperator(operatorId);
        userRepository.createOrUpdate(user);
    }

    private static final class InMemoryUserRepository implements UserRepository {
        private final Map<String, User> users = new HashMap<>();

        @Override
        public UserResponse createOrUpdate(final User user) {
            users.put(user.getUsername(), user);
            return UserResponse.builder()
                    .username(user.getUsername())
                    .departmentCode(user.getDepartmentCode())
                    .enabled(true)
                    .nonExpired(true)
                    .nonLocked(true)
                    .build();
        }

        @Override
        public User findByUsername(final String username) {
            return users.get(username);
        }

        @Override
        public User findByApiKey(final String apiKey) {
            return users.values().stream()
                    .filter(user -> apiKey.equals(user.getApiKey()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public User findById(final UserId userId) {
            return users.values().stream()
                    .filter(user -> userId.equals(user.getUserId()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(users.values());
        }

        @Override
        public List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode) {
            return users.values().stream()
                    .filter(user -> departmentCode.getValue().equals(user.getDepartmentCode().getValue()))
                    .filter(user -> !Boolean.TRUE.equals(user.isDeleted()))
                    .map(User::getUserId)
                    .toList();
        }

        @Override
        public User findByEmail(final String email) {
            return users.values().stream()
                    .filter(user -> email.equals(user.getEmail()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public UserId findInitialUser() {
            return users.values().stream()
                    .filter(User::isInitial)
                    .map(User::getUserId)
                    .findFirst()
                    .orElse(null);
        }
    }

    private static final class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
        private final Map<String, RefreshToken> refreshTokens = new HashMap<>();

        @Override
        public Token save(final RefreshToken refreshToken) {
            refreshToken.setCreatedDate(LocalDateTime.now());
            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(1));
            refreshTokens.put(refreshToken.getToken(), refreshToken);
            return new Token(refreshToken.getToken());
        }

        @Override
        public Optional<RefreshToken> find(final String token) {
            return Optional.ofNullable(refreshTokens.get(token));
        }

        @Override
        public void delete(final String refreshToken) {
            refreshTokens.remove(refreshToken);
        }

        @Override
        public void delete(final LocalDateTime time) {
            refreshTokens.values().removeIf(refreshToken -> !refreshToken.getExpiryDate().isAfter(time));
        }
    }

    private static final class FakeDepartmentServicePort implements DepartmentServicePort {
        private boolean departmentExists = true;

        @Override
        public Boolean departmentExists(final DepartmentCode departmentCode) {
            return departmentExists;
        }

        private void setDepartmentExists(final boolean departmentExists) {
            this.departmentExists = departmentExists;
        }
    }

    private static final class TestRolePermissionService implements RolePermissionService {

        @Override
        public RolePermission findByName(final String name) {
            return permission(UserPermission.valueOf(name));
        }

        @Override
        public Set<RolePermission> findAllAdminPermissions() {
            return Set.of(permission(UserPermission.ROLE_ADMIN_UPDATE));
        }

        @Override
        public Set<RolePermission> findAllManagerPermissions() {
            return Set.of(permission(UserPermission.ROLE_MANAGER_UPDATE));
        }

        @Override
        public Set<RolePermission> findAllSupplierPermissions() {
            return Set.of(permission(UserPermission.ROLE_SUPPLIER_UPDATE));
        }

        private RolePermission permission(final UserPermission permission) {
            return new RolePermission(new RolePermissionId((long) permission.ordinal() + 1), permission);
        }
    }

    private static final class RecordingEventPublisher implements ApplicationEventPublisher {
        @Override
        public void publishEvent(final Object event) {
        }
    }
}
