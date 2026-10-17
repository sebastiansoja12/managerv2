package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.exception.ImpersonationConsentRequiredException;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.vo.ImpersonationSession;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.PlatformOwnerAuthentication;
import com.warehouse.auth.infrastructure.dto.ImpersonationStartedDto;
import com.warehouse.auth.infrastructure.dto.LoginRequestDto;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerProfileDto;
import com.warehouse.auth.infrastructure.dto.StartImpersonationRequestDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/platform/auth")
public class PlatformOwnerAuthenticationController {

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort;

    private final AuthCookieService authCookieService;

    public PlatformOwnerAuthenticationController(final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort,
                                                 final AuthCookieService authCookieService) {
        this.platformOwnerAuthenticationPort = platformOwnerAuthenticationPort;
        this.authCookieService = authCookieService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequestDto loginRequest,
                                      final HttpServletResponse servletResponse) {
        try {
            final PlatformOwnerAuthentication authentication =
                    platformOwnerAuthenticationPort.login(LoginRequest.from(loginRequest));
            authCookieService.addPlatformOwnerAuthenticationCookies(servletResponse, authentication.accessToken(),
                    authentication.refreshToken());
            return noContent();
        } catch (AuthenticationErrorException exception) {
            return unauthorized();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(final HttpServletRequest servletRequest,
                                        final HttpServletResponse servletResponse) {
        final String refreshToken = authCookieService.readPlatformOwnerRefreshToken(servletRequest).orElse(null);
        if (refreshToken == null) {
            authCookieService.clearPlatformOwnerAuthenticationCookies(servletResponse);
            return unauthorized();
        }
        try {
            final PlatformOwnerAuthentication authentication = platformOwnerAuthenticationPort.refresh(refreshToken);
            authCookieService.addPlatformOwnerAuthenticationCookies(servletResponse, authentication.accessToken(),
                    authentication.refreshToken());
            return noContent();
        } catch (AuthenticationErrorException exception) {
            authCookieService.clearPlatformOwnerAuthenticationCookies(servletResponse);
            return unauthorized();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest servletRequest,
                                       final HttpServletResponse servletResponse) {
        try {
            authCookieService.readPlatformOwnerRefreshToken(servletRequest)
                    .ifPresent(platformOwnerAuthenticationPort::logout);
        } finally {
            authCookieService.clearPlatformOwnerAuthenticationCookies(servletResponse);
        }
        return noContent();
    }

    @GetMapping("/me")
    @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
    public ResponseEntity<PlatformOwnerProfileDto> me(@AuthenticationPrincipal final PlatformOwnerId platformOwnerId) {
        final PlatformOwner platformOwner = platformOwnerAuthenticationPort.getPlatformOwner(platformOwnerId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new PlatformOwnerProfileDto(platformOwner.getPlatformOwnerId().value(),
                        platformOwner.getUsername(), platformOwner.getFirstName(), platformOwner.getLastName(),
                        platformOwner.getEmail()));
    }

    @PostMapping("/impersonations")
    @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
    public ResponseEntity<ImpersonationStartedDto> startImpersonation(
            @AuthenticationPrincipal final PlatformOwnerId platformOwnerId,
            @RequestBody final StartImpersonationRequestDto request,
            final HttpServletResponse servletResponse) {
        final ImpersonationSession session = platformOwnerAuthenticationPort.startImpersonation(platformOwnerId,
                OperatorId.of(request.operatorId()));
        authCookieService.addImpersonationAccessCookie(servletResponse, session.accessToken());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new ImpersonationStartedDto(session.userId().value(), session.username(),
                        session.operatorId().value()));
    }

    @ExceptionHandler(ImpersonationConsentRequiredException.class)
    public ResponseEntity<java.util.Map<String, String>> handleConsentRequired(
            final ImpersonationConsentRequiredException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(java.util.Map.of("reason", "CONSENT_" + exception.getReason(), "message", exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    private ResponseEntity<Void> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
    }

    private ResponseEntity<Void> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
    }
}
