package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.service.LoginThrottle;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.infrastructure.dto.LoginRequestDto;
import com.warehouse.auth.infrastructure.dto.RegisterRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final String LOGIN_SCOPE = "user";

    private final AuthenticationPort authenticationPort;

    private final AuthenticationResponseMapper responseMapper;

    private final AuthCookieService authCookieService;

    private final CookieCsrfTokenRepository csrfTokenRepository;
    private final LoginThrottle loginThrottle;

    @PostMapping("/signup")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<?> create(@RequestBody final RegisterRequestDto registerRequest) {
        final RegisterRequest request = RegisterRequest.from(registerRequest);
        final RegisterResponse registerResponse = this.authenticationPort.signup(request);
        return new ResponseEntity<>(responseMapper.map(registerResponse), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequestDto loginRequest,
                                      final HttpServletRequest servletRequest,
                                      final HttpServletResponse servletResponse) {
        final LoginRequest request = LoginRequest.from(loginRequest);
        final String clientAddress = servletRequest.getRemoteAddr();
        if (loginThrottle.isBlocked(LOGIN_SCOPE, request.username(), clientAddress)) {
            log.warn("Login rejected: too many failed attempts");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).cacheControl(CacheControl.noStore()).build();
        }
        try {
            final AuthenticationResponse authentication = authenticationPort.login(request);
            loginThrottle.recordSuccess(LOGIN_SCOPE, request.username());
            authCookieService.addAuthenticationCookies(servletResponse,
                    authentication.accessToken(), authentication.refreshToken());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
        } catch (AuthenticationErrorException | UserNotFoundException exception) {
            loginThrottle.recordFailure(LOGIN_SCOPE, request.username(), clientAddress);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        final String refreshToken = authCookieService.readRefreshToken(request).orElse(null);
        if (refreshToken == null) {
            authCookieService.clearAuthenticationCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        }

        try {
            final AuthenticationResponse authentication = authenticationPort.refresh(refreshToken);
            authCookieService.addAuthenticationCookies(response,
                    authentication.accessToken(), authentication.refreshToken());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
        } catch (AuthenticationErrorException | UserNotFoundException exception) {
            log.info("Session refresh rejected: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        } catch (RuntimeException exception) {
            log.warn("Session refresh failed", exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noStore()).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        try {
            authCookieService.readRefreshToken(request).ifPresent(authenticationPort::logout);
        } finally {
            authCookieService.clearAuthenticationCookies(response);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(final CsrfToken csrfToken,
                                                    final HttpServletRequest request,
                                                    final HttpServletResponse response) {
        csrfTokenRepository.saveToken(csrfToken, request, response);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of("token", csrfToken.getToken()));
    }
}
