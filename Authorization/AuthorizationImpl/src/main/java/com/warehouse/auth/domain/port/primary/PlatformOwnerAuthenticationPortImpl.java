package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.domain.vo.ImpersonationSession;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.PlatformOwnerAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
public class PlatformOwnerAuthenticationPortImpl implements PlatformOwnerAuthenticationPort {

    private final PlatformOwnerRepository platformOwnerRepository;

    private final PlatformOwnerRefreshTokenRepository platformOwnerRefreshTokenRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final ImpersonationConsentPort impersonationConsentPort;

    public PlatformOwnerAuthenticationPortImpl(final PlatformOwnerRepository platformOwnerRepository,
                                               final PlatformOwnerRefreshTokenRepository platformOwnerRefreshTokenRepository,
                                               final UserRepository userRepository,
                                               final JwtService jwtService,
                                               final PasswordEncoder passwordEncoder,
                                               final ImpersonationConsentPort impersonationConsentPort) {
        this.platformOwnerRepository = platformOwnerRepository;
        this.platformOwnerRefreshTokenRepository = platformOwnerRefreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.impersonationConsentPort = impersonationConsentPort;
    }

    @Override
    public PlatformOwnerAuthentication login(final LoginRequest loginRequest) {
        final PlatformOwner platformOwner = platformOwnerRepository.findByUsername(loginRequest.username())
                .filter(PlatformOwner::canAccessOperatorPanel)
                .filter(owner -> passwordEncoder.matches(loginRequest.password(), owner.getPassword()))
                .orElseThrow(() -> new AuthenticationErrorException("Invalid username or password"));
        return issueAuthentication(platformOwner);
    }

    @Override
    public PlatformOwnerAuthentication refresh(final String refreshToken) {
        final PlatformOwnerId platformOwnerId = platformOwnerRefreshTokenRepository.findActivePlatformOwner(refreshToken)
                .orElseThrow(() -> new AuthenticationErrorException("Invalid or expired refresh token"));
        platformOwnerRefreshTokenRepository.delete(refreshToken);
        final PlatformOwner platformOwner = platformOwnerRepository.findById(platformOwnerId)
                .filter(PlatformOwner::canAccessOperatorPanel)
                .orElseThrow(() -> new AuthenticationErrorException("Invalid or expired refresh token"));
        return issueAuthentication(platformOwner);
    }

    @Override
    public void logout(final String refreshToken) {
        platformOwnerRefreshTokenRepository.delete(refreshToken);
    }

    @Override
    public Optional<PlatformOwner> authenticate(final String accessToken) {
        final String username = jwtService.extractUsername(accessToken);
        return platformOwnerRepository.findByUsername(username)
                .filter(owner -> jwtService.isPlatformOwnerTokenValid(accessToken, owner));
    }

    @Override
    public PlatformOwner getPlatformOwner(final PlatformOwnerId platformOwnerId) {
        return platformOwnerRepository.findById(platformOwnerId)
                .filter(PlatformOwner::isActive)
                .orElseThrow(() -> new AuthenticationErrorException("Platform owner does not exist"));
    }

    @Override
    public boolean isActive(final PlatformOwnerId platformOwnerId) {
        return platformOwnerRepository.findById(platformOwnerId)
                .map(PlatformOwner::isActive)
                .orElse(false);
    }

    @Override
    public boolean isImpersonationActive(final Impersonation impersonation, final OperatorId operatorId) {
        return impersonation != null
                && isActive(impersonation.platformOwnerId())
                && impersonationConsentPort.isGranted(impersonation.consentId(), impersonation.platformOwnerId(),
                operatorId);
    }

    @Override
    public ImpersonationSession startImpersonation(final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
        final PlatformOwner platformOwner = getPlatformOwner(platformOwnerId);
        final OrganizationUser user = userRepository.findActiveSuperadmin(operatorId)
                .orElseThrow(() -> new IllegalStateException("Operator " + operatorId + " has no active superadmin"));
        final ImpersonationConsent consent = impersonationConsentPort.requireGrant(platformOwnerId, operatorId);
        final String accessToken = jwtService.generateImpersonationToken(user, platformOwner, consent.getId(),
                consent.getAccessExpiresAt());
        log.info("Platform owner {} started impersonation of user {} in operator {} (consent {})",
                platformOwnerId.value(), user.getUserId().value(), operatorId.value(), consent.getId());
        return new ImpersonationSession(accessToken, user.getUserId(), user.getUsername(), operatorId);
    }

    private PlatformOwnerAuthentication issueAuthentication(final PlatformOwner platformOwner) {
        final String accessToken = jwtService.generatePlatformOwnerToken(platformOwner);
        final String refreshToken = platformOwnerRefreshTokenRepository.create(platformOwner.getPlatformOwnerId());
        return new PlatformOwnerAuthentication(accessToken, refreshToken);
    }
}
