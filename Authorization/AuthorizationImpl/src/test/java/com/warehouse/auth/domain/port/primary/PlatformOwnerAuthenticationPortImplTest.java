package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.exception.ImpersonationConsentRequiredException;
import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.model.ImpersonationConsentStatus;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.domain.vo.ImpersonationSession;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.PlatformOwnerAuthentication;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlatformOwnerAuthenticationPortImplTest {

    private static final PlatformOwnerId PLATFORM_OWNER_ID = new PlatformOwnerId(1L);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final PlatformOwnerRepository platformOwnerRepository = mock(PlatformOwnerRepository.class);

    private final PlatformOwnerRefreshTokenRepository refreshTokenRepository = mock(PlatformOwnerRefreshTokenRepository.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    private final ImpersonationConsentPort impersonationConsentPort = mock(ImpersonationConsentPort.class);

    private JwtService jwtService;

    private PlatformOwnerAuthenticationPort port;

    @BeforeEach
    void setUp() {
        final JwtProvider jwtProvider = new JwtProvider();
        jwtProvider.setSecretKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        jwtProvider.setExpiration(86400000L);
        jwtProvider.setIssuer("manager-v2");
        jwtProvider.setAudience("manager-v2-gui");
        jwtService = new JwtServiceImpl(jwtProvider);
        final PlatformOwner platformOwner = PlatformOwner.create(PLATFORM_OWNER_ID, "owner",
                passwordEncoder.encode("raw-password"), "Platform", "Owner", "owner@test.pl");
        when(platformOwnerRepository.findByUsername("owner")).thenReturn(Optional.of(platformOwner));
        when(platformOwnerRepository.findById(PLATFORM_OWNER_ID)).thenReturn(Optional.of(platformOwner));
        when(refreshTokenRepository.create(PLATFORM_OWNER_ID)).thenReturn("refresh-token");
        port = new PlatformOwnerAuthenticationPortImpl(platformOwnerRepository, refreshTokenRepository, userRepository,
                jwtService, passwordEncoder, impersonationConsentPort);
    }

    @Test
    void shouldIssuePlatformOwnerTokensForValidCredentials() {
        final PlatformOwnerAuthentication authentication = port.login(new LoginRequest("owner", "raw-password"));

        assertThat(authentication.refreshToken()).isEqualTo("refresh-token");
        assertThat(port.authenticate(authentication.accessToken()).map(PlatformOwner::getPlatformOwnerId))
                .contains(PLATFORM_OWNER_ID);
    }

    @Test
    void shouldRejectInvalidPassword() {
        assertThrows(AuthenticationErrorException.class,
                () -> port.login(new LoginRequest("owner", "bad-password")));
    }

    @Test
    void shouldRotateRefreshToken() {
        when(refreshTokenRepository.findActivePlatformOwner("old-refresh-token")).thenReturn(Optional.of(PLATFORM_OWNER_ID));

        final PlatformOwnerAuthentication authentication = port.refresh("old-refresh-token");

        verify(refreshTokenRepository).delete("old-refresh-token");
        assertThat(authentication.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void shouldNotAcceptPlatformOwnerTokenAsOrganizationUserToken() {
        final PlatformOwnerAuthentication authentication = port.login(new LoginRequest("owner", "raw-password"));

        assertThat(jwtService.isTokenValid(authentication.accessToken(), superadmin())).isFalse();
    }

    @Test
    void shouldIssueImpersonationTokenForOperatorSuperadmin() {
        final OrganizationUser superadmin = superadmin();
        when(userRepository.findActiveSuperadmin(OperatorId.of(10L))).thenReturn(Optional.of(superadmin));
        when(impersonationConsentPort.requireGrant(PLATFORM_OWNER_ID, OperatorId.of(10L))).thenReturn(approvedConsent());

        final ImpersonationSession session = port.startImpersonation(PLATFORM_OWNER_ID, OperatorId.of(10L));

        assertThat(session.userId()).isEqualTo(superadmin.getUserId());
        assertThat(jwtService.isTokenValid(session.accessToken(), superadmin)).isTrue();
        assertThat(jwtService.extractImpersonation(session.accessToken()))
                .contains(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L));
    }

    @Test
    void shouldRejectImpersonationWithoutOperatorConsent() {
        when(userRepository.findActiveSuperadmin(OperatorId.of(10L))).thenReturn(Optional.of(superadmin()));
        when(impersonationConsentPort.requireGrant(PLATFORM_OWNER_ID, OperatorId.of(10L)))
                .thenThrow(new ImpersonationConsentRequiredException("REJECTED"));

        assertThrows(ImpersonationConsentRequiredException.class,
                () -> port.startImpersonation(PLATFORM_OWNER_ID, OperatorId.of(10L)));
    }

    @Test
    void shouldTreatImpersonationAsActiveOnlyWithGrantedConsent() {
        when(impersonationConsentPort.isGranted(77L, PLATFORM_OWNER_ID, OperatorId.of(10L))).thenReturn(true);

        assertThat(port.isImpersonationActive(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L), OperatorId.of(10L)))
                .isTrue();
        assertThat(port.isImpersonationActive(new Impersonation(PLATFORM_OWNER_ID, "owner", 78L), OperatorId.of(10L)))
                .isFalse();
    }

    private ImpersonationConsent approvedConsent() {
        final Instant now = Instant.now();
        return ImpersonationConsent.restore(77L, OperatorId.of(10L), PLATFORM_OWNER_ID, "owner", null,
                ImpersonationConsentStatus.APPROVED, now, now.plusSeconds(86400), new UserId(5L), "superadmin", now,
                now.plusSeconds(3600));
    }

    @Test
    void shouldRejectImpersonationWhenOperatorHasNoSuperadmin() {
        when(userRepository.findActiveSuperadmin(OperatorId.of(11L))).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> port.startImpersonation(PLATFORM_OWNER_ID, OperatorId.of(11L)));
    }

    private OrganizationUser superadmin() {
        final OrganizationUser user = OrganizationUser.of(new UserId(5L), "superadmin", "password", "Super", "Admin",
                "superadmin@test.pl", OrganizationUser.Role.SUPERADMIN, new DepartmentId(10L), "api-key");
        user.assignOperator(OperatorId.of(10L));
        user.setDeleted(false);
        return user;
    }
}
